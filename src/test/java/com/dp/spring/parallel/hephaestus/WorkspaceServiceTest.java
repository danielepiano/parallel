package com.dp.spring.parallel.hephaestus;

import com.dp.spring.parallel.common.exceptions.WorkspaceNameAlreadyExistsInHeadquartersException;
import com.dp.spring.parallel.common.exceptions.WorkspaceNotFoundException;
import com.dp.spring.parallel.common.fixtures.HeadquartersFixture;
import com.dp.spring.parallel.common.fixtures.WorkspaceFixture;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateWorkspaceRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateWorkspaceRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.parallel.hephaestus.database.enums.WorkspaceType;
import com.dp.spring.parallel.hephaestus.database.repositories.WorkspaceRepository;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.hephaestus.services.WorkplaceService;
import com.dp.spring.parallel.hephaestus.services.impl.WorkspaceServiceImpl;
import com.dp.spring.parallel.hestia.database.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceTest {

    @Spy
    WorkplaceService workplaceService;
    @Spy
    HeadquartersService headquartersService;

    @Spy
    WorkspaceRepository workspaceRepository;

    @InjectMocks
    @Spy
    WorkspaceServiceImpl workspaceService;


    // Mock
    void mockGetPrincipal(User user) {
        doReturn(user)
                .when(workspaceService)
                .getPrincipalOrThrow();
    }

    void mockCheckPrincipalScope() {
        doNothing().when(workspaceService).checkPrincipalScopeOrThrow(any());
    }

    void mockGetAndCheckHeadquarters() {
        doReturn(HeadquartersFixture.get())
                .when(headquartersService)
                .headquarters(anyInt());
        mockCheckPrincipalScope();
    }

    @BeforeEach
    public void setUp() {
    }

    @Test
    void add_whenConflict_shouldThrow() {
        Integer headquartersId = 2;
        CreateWorkspaceRequestDTO request = CreateWorkspaceRequestDTO.builder()
                .name("name-test")
                .description("description-test")
                .type(WorkspaceType.OPEN_SPACE)
                .floor("floor-test")
                .build();

        mockGetAndCheckHeadquarters();
        doReturn(true)
                .when(workspaceRepository)
                .existsByNameAndHeadquarters(anyString(), any(Headquarters.class));

        assertThrows(WorkspaceNameAlreadyExistsInHeadquartersException.class, () -> workspaceService.add(headquartersId, request));
    }

    @Test
    void add_whenOk_shouldWork() {
        Integer headquartersId = 2;
        CreateWorkspaceRequestDTO request = CreateWorkspaceRequestDTO.builder()
                .name("name-test")
                .description("description-test")
                .type(WorkspaceType.OPEN_SPACE)
                .floor("floor-test")
                .build();

        mockGetAndCheckHeadquarters();
        doReturn(false)
                .when(workspaceRepository)
                .existsByNameAndHeadquarters(anyString(), any(Headquarters.class));

        workspaceService.add(headquartersId, request);

        verify(workspaceRepository).save(any(Workspace.class));
    }

    @Test
    void workspace_wsId_whenNotFound_shouldThrow() {
        Integer workspaceId = 3;

        doReturn(Optional.empty())
                .when(workspaceRepository)
                .findById(anyInt());

        assertThrows(WorkspaceNotFoundException.class, () -> workspaceService.workspace(workspaceId));
    }

    @Test
    void workspace_wsId_whenFound_shouldWork() {
        Integer workspaceId = 3;

        doReturn(ofNullable(WorkspaceFixture.get()))
                .when(workspaceRepository)
                .findById(anyInt());

        assertDoesNotThrow(() -> workspaceService.workspace(workspaceId));
    }

    @Test
    void workspace_whenNotFound_shouldThrow() {
        Integer headquartersId = 2;
        Integer workspaceId = 3;

        doReturn(HeadquartersFixture.get())
                .when(headquartersService)
                .headquarters(anyInt());
        doReturn(empty())
                .when(workspaceRepository)
                .findByIdAndHeadquarters(anyInt(), any(Headquarters.class));

        assertThrows(WorkspaceNotFoundException.class, () -> workspaceService.workspace(headquartersId, workspaceId));
    }

    @Test
    void workspace_whenFound_shouldWork() {
        Integer headquartersId = 2;
        Integer workspaceId = 3;

        doReturn(HeadquartersFixture.get())
                .when(headquartersService)
                .headquarters(anyInt());
        doReturn(ofNullable(WorkspaceFixture.get()))
                .when(workspaceRepository)
                .findByIdAndHeadquarters(anyInt(), any(Headquarters.class));

        assertDoesNotThrow(() -> workspaceService.workspace(headquartersId, workspaceId));
    }

    @Test
    void workspaces_when_should() {
        Integer headquartersId = 2;

        doReturn(HeadquartersFixture.get())
                .when(headquartersService)
                .headquarters(anyInt());
        doReturn(List.of(WorkspaceFixture.get()))
                .when(workspaceRepository)
                .findAllByHeadquarters(any(Headquarters.class), any(Sort.class));

        workspaceService.workspaces(headquartersId);

        verify(workspaceRepository).findAllByHeadquarters(any(Headquarters.class), any(Sort.class));
    }

    @Test
    void update_whenConflict_shouldThrow() {
        Integer headquartersId = 2;
        Integer workspaceId = 3;
        UpdateWorkspaceRequestDTO request = UpdateWorkspaceRequestDTO.builder()
                .name("name-test")
                .description("description-test")
                .type(WorkspaceType.OPEN_SPACE)
                .floor("floor-test")
                .build();

        mockGetAndCheckHeadquarters();
        doReturn(true)
                .when(workspaceRepository)
                .existsByIdNotAndNameAndHeadquarters(anyInt(), anyString(), any(Headquarters.class));

        assertThrows(WorkspaceNameAlreadyExistsInHeadquartersException.class, () -> workspaceService.update(headquartersId, workspaceId, request));
    }

    @Test
    void update_whenNotFound_shouldThrow() {
        Integer headquartersId = 2;
        Integer workspaceId = 3;
        UpdateWorkspaceRequestDTO request = UpdateWorkspaceRequestDTO.builder()
                .name("name-test")
                .description("description-test")
                .type(WorkspaceType.OPEN_SPACE)
                .floor("floor-test")
                .build();

        mockGetAndCheckHeadquarters();
        doReturn(false)
                .when(workspaceRepository)
                .existsByIdNotAndNameAndHeadquarters(anyInt(), anyString(), any(Headquarters.class));
        doReturn(Optional.empty())
                .when(workspaceRepository)
                .findByIdAndHeadquarters(anyInt(), any(Headquarters.class));

        assertThrows(WorkspaceNotFoundException.class, () -> workspaceService.update(headquartersId, workspaceId, request));
    }

    @Test
    void update_whenOk_shouldWork() {
        Integer headquartersId = 2;
        Integer workspaceId = 3;
        UpdateWorkspaceRequestDTO request = UpdateWorkspaceRequestDTO.builder()
                .name("name-test")
                .description("description-test")
                .type(WorkspaceType.OPEN_SPACE)
                .floor("floor-test")
                .build();

        mockGetAndCheckHeadquarters();
        doReturn(false)
                .when(workspaceRepository)
                .existsByIdNotAndNameAndHeadquarters(anyInt(), anyString(), any(Headquarters.class));
        doReturn(ofNullable(WorkspaceFixture.get()))
                .when(workspaceRepository)
                .findByIdAndHeadquarters(anyInt(), any(Headquarters.class));

        workspaceService.update(headquartersId, workspaceId, request);
        verify(workspaceRepository).save(any(Workspace.class));
    }

    @Test
    void remove_whenFound_shouldWork() {
        Integer headquartersId = 2;
        Integer workspaceId = 3;

        mockGetAndCheckHeadquarters();
        doReturn(ofNullable(WorkspaceFixture.get()))
                .when(workspaceRepository)
                .findByIdAndHeadquarters(anyInt(), any(Headquarters.class));

        workspaceService.remove(headquartersId, workspaceId);

        verify(workplaceService).removeAll(any(Workspace.class));
        verify(workspaceRepository).softDelete(any(Workspace.class));
    }

    @Test
    void remove_whenNotFound_shouldDoNothing() {
        Integer headquartersId = 2;
        Integer workspaceId = 3;

        mockGetAndCheckHeadquarters();
        doReturn(Optional.empty())
                .when(workspaceRepository)
                .findByIdAndHeadquarters(anyInt(), any(Headquarters.class));

        workspaceService.remove(headquartersId, workspaceId);

        verify(workplaceService, times(0)).removeAll(any(Workspace.class));
        verify(workspaceRepository, times(0)).softDelete(any(Workspace.class));
    }

    @Test
    void removeAll_when_should() {
        Headquarters headquarters = HeadquartersFixture.get();

        doReturn(Set.of(WorkspaceFixture.get()))
                .when(workspaceRepository)
                .findAllByHeadquarters(any(Headquarters.class));

        workspaceService.removeAll(headquarters);

        verify(workplaceService).removeAll(any(Workspace.class));
        verify(workspaceRepository).softDelete(any(Workspace.class));
    }

}
