package com.dp.spring.parallel.hephaestus;

import com.dp.spring.parallel.common.exceptions.WorkplaceNameAlreadyExistsInWorkspaceException;
import com.dp.spring.parallel.common.exceptions.WorkplaceNotFoundException;
import com.dp.spring.parallel.common.fixtures.HeadquartersFixture;
import com.dp.spring.parallel.common.fixtures.WorkplaceFixture;
import com.dp.spring.parallel.common.fixtures.WorkspaceFixture;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateWorkplaceRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateWorkplaceRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.parallel.hephaestus.database.enums.WorkplaceType;
import com.dp.spring.parallel.hephaestus.database.repositories.WorkplaceRepository;
import com.dp.spring.parallel.hephaestus.services.WorkspaceService;
import com.dp.spring.parallel.hephaestus.services.impl.WorkplaceServiceImpl;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.mnemosyne.services.WorkplaceBookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkplaceServiceTest {

    @Spy
    WorkspaceService workspaceService;
    @Spy
    WorkplaceBookingService workplaceBookingService;

    @Spy
    WorkplaceRepository workplaceRepository;

    @InjectMocks
    @Spy
    WorkplaceServiceImpl workplaceService;


    // Mock
    void mockGetPrincipal(User user) {
        doReturn(user)
                .when(workplaceService)
                .getPrincipalOrThrow();
    }

    void mockCheckPrincipalScope() {
        doNothing().when(workplaceService).checkPrincipalScopeOrThrow(any());
    }



    @BeforeEach
    public void setUp() {
    }

    @Test
    void workplace_whenNotFound_shouldThrow() {
        Integer headquartersId = 2;
        Integer workspaceId = 3;
        Integer workplaceId = 4;

        doReturn(WorkspaceFixture.get())
                .when(workspaceService)
                .workspace(headquartersId, workspaceId);
        doReturn(Optional.empty())
                .when(workplaceRepository)
                .findByIdAndWorkspace(eq(workplaceId), any(Workspace.class));

        assertThrows(WorkplaceNotFoundException.class, () -> workplaceService.workplace(headquartersId, workspaceId, workplaceId));
    }

    @Test
    void workplace_whenFound_shouldWork() {
        Integer headquartersId = 2;
        Integer workspaceId = 3;
        Integer workplaceId = 4;

        doReturn(WorkspaceFixture.get())
                .when(workspaceService)
                .workspace(headquartersId, workspaceId);
        doReturn(ofNullable(WorkplaceFixture.get()))
                .when(workplaceRepository)
                .findByIdAndWorkspace(eq(workplaceId), any(Workspace.class));

        assertDoesNotThrow(() -> workplaceService.workplace(headquartersId, workspaceId, workplaceId));
    }

    @Test
    void workplaces_shouldWork() {
        Integer headquartersId = 2;
        Integer workspaceId = 3;

        doReturn(WorkspaceFixture.get())
                .when(workspaceService)
                .workspace(headquartersId, workspaceId);
        doReturn(List.of(WorkplaceFixture.get()))
                .when(workplaceRepository)
                .findAllByWorkspace(any(Workspace.class), any(Sort.class));

        var result = workplaceService.workplaces(headquartersId, workspaceId);

        assertEquals(1, result.size(), "return not coherent");
    }

    @Test
    void countForHeadquarters_shouldWork() {
        Headquarters headquarters = HeadquartersFixture.get();

        doReturn(3L)
                .when(workplaceRepository)
                .countByWorkspaceHeadquarters(headquarters);

        var count = workplaceService.countForHeadquarters(headquarters);

        assertEquals(3L, count, "count not matching");
    }

    @Test
    void countAvailableOnTotalForHeadquarters_shouldWork() {
        Headquarters headquarters = HeadquartersFixture.get();
        LocalDate onDate = LocalDate.now();

        long notAvailable = 7L;
        doReturn(notAvailable)
                .when(workplaceRepository)
                .countNotAvailableByWorkspaceHeadquartersAndBookingDate(headquarters, onDate);
        long total = 10L;
        doReturn(total)
                .when(workplaceRepository)
                .countByWorkspaceHeadquarters(headquarters);

        var availableOnTotal = workplaceService.countAvailableOnTotalForHeadquarters(headquarters, onDate);

        assertEquals(Pair.of(total - notAvailable, total), availableOnTotal, "availableness not matching");
    }

    @Test
    void countAvailableOnTotal_shouldWork() {
        Workspace workspace = WorkspaceFixture.get();
        LocalDate onDate = LocalDate.now();

        long notAvailable = 7L;
        doReturn(notAvailable)
                .when(workplaceRepository)
                .countNotAvailableByWorkspaceAndBookingDate(workspace, onDate);
        long total = 10L;
        doReturn(total)
                .when(workplaceRepository)
                .countByWorkspace(workspace);

        var availableOnTotal = workplaceService.countAvailableOnTotal(workspace, onDate);

        assertEquals(Pair.of(total - notAvailable, total), availableOnTotal, "availableness not matching");
    }

    @Test
    void remove_shouldWork() {
        Integer headquartersId = 2;
        Integer workspaceId = 3;
        Integer workplaceId = 4;

        Workspace workspace = WorkspaceFixture.get();
        doReturn(workspace)
                .when(workspaceService)
                .workspace(headquartersId, workspaceId);
        doReturn(ofNullable(WorkplaceFixture.get()))
                .when(workplaceRepository)
                .findByIdAndWorkspace(workplaceId,  workspace);

        workplaceService.remove(headquartersId, workspaceId, workplaceId);

        verify(workplaceBookingService).cancelAll(any(Workplace.class));
        verify(workplaceRepository).softDelete(any(Workplace.class));
    }

    @Test
    void removeAll_shouldWork() {
        Workspace workspace = WorkspaceFixture.get();

        doReturn(Set.of(WorkplaceFixture.get()))
                .when(workplaceRepository)
                .findAllByWorkspace(workspace);

        workplaceService.removeAll(workspace);

        verify(workplaceBookingService).cancelAll(any(Workplace.class));
        verify(workplaceRepository).softDelete(any(Workplace.class));
    }

    @Test
    void availableWorkplaces_shouldWork() {
        Integer headquartersId = 2;
        Integer workspaceId = 3;
        LocalDate onDate = LocalDate.now();


        Workspace workspace = WorkspaceFixture.get();
        doReturn(workspace)
                .when(workspaceService)
                .workspace(headquartersId, workspaceId);
        doReturn(List.of(WorkplaceFixture.get()))
                .when(workplaceRepository)
                .findAllAvailableByWorkspaceAndBookingDate(workspace, onDate);

        var result = workplaceService.availableWorkplaces(headquartersId, workspaceId, onDate);

        assertEquals(1, result.size(), "size not matching");
    }

    @Test
    void add_whenConflict_shouldThrow() {
        Integer headquartersId = 2;
        Integer workspaceId = 3;
        CreateWorkplaceRequestDTO request = CreateWorkplaceRequestDTO.builder()
                .name("name-test")
                .description("description-test")
                .type(WorkplaceType.DESK)
                .build();

        Workspace workspace = WorkspaceFixture.get();
        doReturn(workspace)
                .when(workspaceService)
                .workspace(headquartersId, workspaceId);
        doReturn(true)
                .when(workplaceRepository)
                .existsByNameAndWorkspace(request.getName(), workspace);

        assertThrows(WorkplaceNameAlreadyExistsInWorkspaceException.class, () -> workplaceService.add(headquartersId, workspaceId, request));
    }

    @Test
    void add_whenOk_shouldWork() {
        Integer headquartersId = 2;
        Integer workspaceId = 3;
        CreateWorkplaceRequestDTO request = CreateWorkplaceRequestDTO.builder()
                .name("name-test")
                .description("description-test")
                .type(WorkplaceType.DESK)
                .build();

        Workspace workspace = WorkspaceFixture.get();
        doReturn(workspace)
                .when(workspaceService)
                .workspace(headquartersId, workspaceId);
        doReturn(false)
                .when(workplaceRepository)
                .existsByNameAndWorkspace(request.getName(), workspace);

        workplaceService.add(headquartersId, workspaceId, request);
        verify(workplaceRepository).save(any(Workplace.class));
    }

    @Test
    void update_whenConflict_shouldThrow() {
        Integer headquartersId = 2;
        Integer workspaceId = 3;
        Integer workplaceId = 4;
        UpdateWorkplaceRequestDTO request = UpdateWorkplaceRequestDTO.builder()
                .name("name-test")
                .description("description-test")
                .type(WorkplaceType.DESK)
                .build();

        Workspace workspace = WorkspaceFixture.get();
        doReturn(workspace)
                .when(workspaceService)
                .workspace(headquartersId, workspaceId);
        doReturn(true)
                .when(workplaceRepository)
                .existsByIdNotAndNameAndWorkspace(workplaceId, request.getName(), workspace);

        assertThrows(WorkplaceNameAlreadyExistsInWorkspaceException.class, () -> workplaceService.update(headquartersId, workspaceId, workplaceId, request));
    }

    @Test
    void update_whenOk_shouldWork() {
        Integer headquartersId = 2;
        Integer workspaceId = 3;
        Integer workplaceId = 4;
        UpdateWorkplaceRequestDTO request = UpdateWorkplaceRequestDTO.builder()
                .name("name-test")
                .description("description-test")
                .type(WorkplaceType.DESK)
                .build();

        Workspace workspace = WorkspaceFixture.get();
        doReturn(workspace)
                .when(workspaceService)
                .workspace(headquartersId, workspaceId);
        doReturn(false)
                .when(workplaceRepository)
                .existsByIdNotAndNameAndWorkspace(workplaceId, request.getName(), workspace);
        doReturn(ofNullable(WorkplaceFixture.get()))
                .when(workplaceRepository)
                .findByIdAndWorkspace(workplaceId, workspace);

        workplaceService.update(headquartersId, workspaceId, workplaceId, request);
        verify(workplaceRepository).save(any(Workplace.class));
    }
}
