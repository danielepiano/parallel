package com.dp.spring.parallel.hephaestus;

import com.dp.spring.parallel.agora.services.EventService;
import com.dp.spring.parallel.agora.services.observer.HeadquartersEventsObserverService;
import com.dp.spring.parallel.common.exceptions.CompanyNotFoundException;
import com.dp.spring.parallel.common.exceptions.HeadquartersAlreadyExistsException;
import com.dp.spring.parallel.common.exceptions.HeadquartersNotDeletableException;
import com.dp.spring.parallel.common.exceptions.HeadquartersNotFoundException;
import com.dp.spring.parallel.common.fixtures.CompanyFixture;
import com.dp.spring.parallel.common.fixtures.EventFixture;
import com.dp.spring.parallel.common.fixtures.HeadquartersFixture;
import com.dp.spring.parallel.common.fixtures.UserFixture;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateHeadquartersRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateHeadquartersRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.repositories.HeadquartersRepository;
import com.dp.spring.parallel.hephaestus.services.CompanyService;
import com.dp.spring.parallel.hephaestus.services.WorkspaceService;
import com.dp.spring.parallel.hephaestus.services.impl.HeadquartersServiceImpl;
import com.dp.spring.parallel.hermes.services.notification.impl.EmailNotificationService;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.services.HeadquartersReceptionistUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HeadquartersServiceTest {

    @Spy
    HeadquartersReceptionistUserService headquartersReceptionistUserService;
    @Spy
    CompanyService companyService;
    @Spy
    WorkspaceService workspaceService;
    @Spy
    EventService eventService;

    @Spy
    HeadquartersRepository headquartersRepository;

    @Spy
    HeadquartersEventsObserverService headquartersEventsObserverService = new HeadquartersEventsObserverService(new EmailNotificationService(new JavaMailSenderImpl()));

    @InjectMocks
    @Spy
    HeadquartersServiceImpl headquartersService;


    // Mock
    void mockGetPrincipal(User user) {
        doReturn(user)
                .when(headquartersService)
                .getPrincipalOrThrow();
    }

    void mockCheckPrincipalScope() {
        doNothing().when(headquartersService).checkPrincipalScopeOrThrow(anyInt());
    }

    @BeforeEach
    public void setUp() {
    }

    @Test
    void add_whenConflict_shouldThrow() {
        Integer companyId = 1;
        CreateHeadquartersRequestDTO request = CreateHeadquartersRequestDTO.builder()
                .city("city-test")
                .address("address-test")
                .description("description-test")
                .phoneNumber("number-test")
                .build();

        doReturn(CompanyFixture.get())
                .when(companyService)
                .company(anyInt());
        mockCheckPrincipalScope();
        doReturn(true)
                .when(headquartersRepository)
                .existsByCityAndAddressAndCompany(anyString(), anyString(), any(Company.class));

        assertThrows(HeadquartersAlreadyExistsException.class, () -> headquartersService.add(companyId, request));
    }

    @Test
    void add_whenOk_shouldSave() {
        Integer companyId = 1;
        CreateHeadquartersRequestDTO request = CreateHeadquartersRequestDTO.builder()
                .city("city-test")
                .address("address-test")
                .description("description-test")
                .phoneNumber("number-test")
                .build();

        doReturn(CompanyFixture.get())
                .when(companyService)
                .company(anyInt());
        mockCheckPrincipalScope();
        doReturn(false)
                .when(headquartersRepository)
                .existsByCityAndAddressAndCompany(anyString(), anyString(), any(Company.class));

        headquartersService.add(companyId, request);

        verify(headquartersRepository).save(any(Headquarters.class));
    }

    @Test
    void headquarters_one_whenNotFound_shouldThrow() {
        Integer headquartersId = 2;

        doReturn(Optional.empty())
                .when(headquartersRepository)
                .findById(anyInt());

        assertThrows(HeadquartersNotFoundException.class, () -> headquartersService.headquarters(headquartersId));
    }

    @Test
    void headquarters_one_whenExists_shouldThrow() {
        Integer headquartersId = 2;

        doReturn(ofNullable(HeadquartersFixture.get()))
                .when(headquartersRepository)
                .findById(anyInt());

        assertDoesNotThrow(() -> headquartersService.headquarters(headquartersId));
    }

    @Test
    void headquarters_list_whenAdmin_shouldWork() {
        mockGetPrincipal(UserFixture.getAdmin());

        doReturn(List.of(HeadquartersFixture.get()))
                .when(headquartersRepository)
                .findAll();

        headquartersService.headquarters();

        verify(headquartersRepository).findAll();
        assertDoesNotThrow(() -> headquartersService.headquarters());
    }

    @Test
    void headquarters_list_whenCompanyManager_shouldWork() {
        mockGetPrincipal(UserFixture.getCompanyManager());
        List<Headquarters> list = new ArrayList<>();
        list.add(HeadquartersFixture.get());
        doReturn(list)
                .when(headquartersRepository)
                .findAll();

        assertDoesNotThrow(() -> headquartersService.headquarters());

        verify(headquartersRepository).findAll();
    }

    @Test
    void favoriteHeadquarters_shouldWork() {
        mockGetPrincipal(UserFixture.getCompanyManager());

        headquartersService.favoriteHeadquarters();
        verify(headquartersService).favoriteHeadquarters();
        assertDoesNotThrow(() -> headquartersService.favoriteHeadquarters());
    }

    @Test
    void companyHeadquarters_whenCompanyNotFound_shouldThrow() {
        Integer companyId = 1;

        doThrow(CompanyNotFoundException.class)
                .when(companyService)
                .company(anyInt());

        assertThrows(CompanyNotFoundException.class, () -> headquartersService.companyHeadquarters(companyId));

        verify(companyService).company(anyInt());
        //verify(headquartersRepository).findAllByCompany(any(Company.class));
    }

    @Test
    void companyHeadquarters_whenOk_shouldWork() {
        Integer companyId = 1;

        doReturn(CompanyFixture.get())
                .when(companyService)
                .company(anyInt());

        headquartersService.companyHeadquarters(companyId);

        verify(companyService).company(anyInt());
        verify(headquartersRepository).findAllByCompany(any(Company.class));
    }

    @Test
    void update_whenNotFound_shouldThrow() {
        Integer companyId = 1;
        Integer headquartersId = 2;
        UpdateHeadquartersRequestDTO request = UpdateHeadquartersRequestDTO.builder()
                .city("city-test")
                .address("address-test")
                .description("description-test")
                .phoneNumber("number-test")
                .build();

        doReturn(CompanyFixture.get())
                .when(companyService)
                .company(anyInt());

        doReturn(Optional.empty())
                .when(headquartersRepository)
                .findByIdAndCompany(anyInt(), any(Company.class));

        assertThrows(HeadquartersNotFoundException.class, () -> headquartersService.update(companyId, headquartersId, request));
    }

    @Test
    void update_whenConflict_shouldThrow() {
        Integer companyId = 1;
        Integer headquartersId = 2;
        UpdateHeadquartersRequestDTO request = UpdateHeadquartersRequestDTO.builder()
                .city("city-test")
                .address("address-test")
                .description("description-test")
                .phoneNumber("number-test")
                .build();

        mockCheckPrincipalScope();

        doReturn(CompanyFixture.get())
                .when(companyService)
                .company(anyInt());

        doReturn(ofNullable(HeadquartersFixture.get()))
                .when(headquartersRepository)
                .findByIdAndCompany(anyInt(), any(Company.class));

        doReturn(true)
                .when(headquartersRepository)
                .existsByIdNotAndCityAndAddressAndCompany(anyInt(), anyString(), anyString(), any(Company.class));

        //headquartersService.update(companyId, headquartersId, request);
        assertThrows(HeadquartersAlreadyExistsException.class, () -> headquartersService.update(companyId, headquartersId, request));
        //verify(headquartersRepository).save(any(Headquarters.class));
    }

    @Test
    void update_whenOk_shouldWork() {
        Integer companyId = 1;
        Integer headquartersId = 2;
        UpdateHeadquartersRequestDTO request = UpdateHeadquartersRequestDTO.builder()
                .city("city-test")
                .address("address-test")
                .description("description-test")
                .phoneNumber("number-test")
                .build();

        mockCheckPrincipalScope();

        doReturn(CompanyFixture.get())
                .when(companyService)
                .company(anyInt());

        doReturn(ofNullable(HeadquartersFixture.get()))
                .when(headquartersRepository)
                .findByIdAndCompany(anyInt(), any(Company.class));

        doReturn(false)
                .when(headquartersRepository)
                .existsByIdNotAndCityAndAddressAndCompany(anyInt(), anyString(), anyString(), any(Company.class));

        headquartersService.update(companyId, headquartersId, request);
        verify(headquartersRepository).save(any(Headquarters.class));
    }

    @Test
    void remove_whenOk_shouldWork() {
        Integer companyId = 1;
        Integer headquartersId = 2;

        doReturn(CompanyFixture.get())
                .when(companyService)
                .company(anyInt());

        doReturn(ofNullable(HeadquartersFixture.get()))
                .when(headquartersRepository)
                .findByIdAndCompany(anyInt(), any(Company.class));

        mockCheckPrincipalScope();

        doReturn(emptySet())
                .when(headquartersReceptionistUserService)
                .headquartersReceptionistsFor(any(Headquarters.class));

        headquartersService.remove(companyId, headquartersId);

        verify(workspaceService).removeAll(any(Headquarters.class));
        verify(eventService).removeAll(any(Headquarters.class));
        verify(headquartersRepository).softDelete(any(Headquarters.class));
    }

    @Test
    void remove_whenNotDeletable_shouldThrow() {
        Integer companyId = 1;
        Integer headquartersId = 2;

        doReturn(CompanyFixture.get())
                .when(companyService)
                .company(anyInt());

        doReturn(ofNullable(HeadquartersFixture.get()))
                .when(headquartersRepository)
                .findByIdAndCompany(anyInt(), any(Company.class));

        mockCheckPrincipalScope();

        doReturn(Set.of(UserFixture.getHeadquartersReceptionist()))
                .when(headquartersReceptionistUserService)
                .headquartersReceptionistsFor(any(Headquarters.class));

        assertThrows(HeadquartersNotDeletableException.class, () -> headquartersService.remove(companyId, headquartersId));
    }

    @Test
    void removeAll_whenOk_shouldWork() {
        Company company = CompanyFixture.get();

        doReturn(Set.of(HeadquartersFixture.get()))
                .when(headquartersRepository)
                .findAllByCompany(any(Company.class));

        doReturn(emptySet())
                .when(headquartersReceptionistUserService)
                .headquartersReceptionistsFor(any(Headquarters.class));

        headquartersService.removeAll(company);

        verify(workspaceService).removeAll(any(Headquarters.class));
        verify(eventService).removeAll(any(Headquarters.class));
        verify(headquartersRepository).softDelete(any(Headquarters.class));
    }

    @Test
    void toggleFavouriteHeadquarters_whenNoObserver_shouldCallAddObserver() {
        Integer headquartersId = 2;

        doReturn(HeadquartersFixture.get())
                .when(headquartersService)
                .headquarters(anyInt());
        mockGetPrincipal(UserFixture.getCompanyManager());
        doNothing()
                .when(headquartersService)
                .addObserver(any(User.class), any(Headquarters.class));

        headquartersService.toggleFavouriteHeadquarters(headquartersId);

        verify(headquartersService).addObserver(any(User.class), any(Headquarters.class));
    }

    @Test
    void toggleFavouriteHeadquarters_whenObserver_shouldCallRemoveObserver() {
        Integer headquartersId = 2;

        doReturn(HeadquartersFixture.getWithObservers())
                .when(headquartersService)
                .headquarters(anyInt());
        mockGetPrincipal(UserFixture.getCompanyManager());
        doNothing()
                .when(headquartersService)
                .removeObserver(any(User.class), any(Headquarters.class));

        headquartersService.toggleFavouriteHeadquarters(headquartersId);

        verify(headquartersService).removeObserver(any(User.class), any(Headquarters.class));
    }

    @Test
    void addObserver_whenOk_shouldWork() {
        User user = UserFixture.getCompanyManager();
        Headquarters headquarters = HeadquartersFixture.get();

        headquartersService.addObserver(user, headquarters);

        assertEquals(1, headquarters.getObservers().size(), "observer not added");
        verify(headquartersRepository).save(any(Headquarters.class));
    }

    @Test
    void removeObserver_whenOk_shouldWork() {
        User user = UserFixture.getCompanyManager();
        Headquarters headquarters = HeadquartersFixture.getWithObservers();

        headquartersService.removeObserver(user, headquarters);

        assertEquals(0, headquarters.getObservers().size(), "observer not removed");
        verify(headquartersRepository).save(any(Headquarters.class));
    }

    @Test
    void notifyObservers_when_should() {
        Headquarters headquarters = HeadquartersFixture.getWithObservers();
        HeadquartersEventsObserverService.Context context = HeadquartersEventsObserverService.Context.builder()
                .createdEvent(EventFixture.get())
                .build();

        doNothing().when(headquartersEventsObserverService)
                .react(any(User.class), any(Headquarters.class), any(HeadquartersEventsObserverService.Context.class));

        headquartersService.notifyObservers(headquarters, headquartersEventsObserverService, context);
    }

    @Test
    void checkExistence_whenNotFound_shouldThrow() {
        Integer companyId = 1;
        Integer headquartersId = 2;

        doReturn(false)
                .when(headquartersRepository)
                .existsByIdAndCompanyId(headquartersId, companyId);

        assertThrows(HeadquartersNotFoundException.class, () -> headquartersService.checkExistence(companyId, headquartersId));
    }

    @Test
    void checkExistence_whenFound_shouldWork() {
        Integer companyId = 1;
        Integer headquartersId = 2;

        doReturn(true)
                .when(headquartersRepository)
                .existsByIdAndCompanyId(headquartersId, companyId);

        assertDoesNotThrow(() -> headquartersService.checkExistence(companyId, headquartersId));
    }

}
