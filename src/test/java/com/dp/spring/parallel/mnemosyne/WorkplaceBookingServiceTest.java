package com.dp.spring.parallel.mnemosyne;

import com.dp.spring.parallel.common.exceptions.*;
import com.dp.spring.parallel.common.fixtures.*;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.parallel.hephaestus.database.repositories.WorkplaceRepository;
import com.dp.spring.parallel.hephaestus.database.repositories.WorkspaceRepository;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.hephaestus.services.WorkplaceService;
import com.dp.spring.parallel.hephaestus.services.WorkspaceService;
import com.dp.spring.parallel.hermes.services.notification.impl.EmailNotificationService;
import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.mnemosyne.api.dtos.WorkplaceBookingRequestDTO;
import com.dp.spring.parallel.mnemosyne.database.entities.WorkplaceBooking;
import com.dp.spring.parallel.mnemosyne.database.repositories.WorkplaceBookingRepository;
import com.dp.spring.parallel.mnemosyne.services.impl.WorkplaceBookingServiceImpl;
import com.dp.spring.parallel.mnemosyne.services.observer.HeadquartersWorkplaceBookingsObserverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkplaceBookingServiceTest {

    @Mock
    EmailNotificationService emailNotificationService;
    @Spy
    HeadquartersService headquartersService;
    @Spy
    WorkplaceService workplaceService;
    @Spy
    WorkspaceService workspaceService;

    @Spy
    WorkspaceRepository workspaceRepository;
    @Spy
    WorkplaceRepository workplaceRepository;
    @Spy
    WorkplaceBookingRepository workplaceBookingRepository;


    @Spy
    HeadquartersWorkplaceBookingsObserverService headquartersWorkplaceBookingsObserverService = new HeadquartersWorkplaceBookingsObserverService(emailNotificationService);

    @InjectMocks
    @Spy
    WorkplaceBookingServiceImpl workplaceBookingService;


    // Mock
    void mockGetPrincipal(User user) {
        doReturn(user)
                .when(workplaceBookingService)
                .getPrincipalOrThrow();
    }

    void mockGetWorkplace() {
        doReturn(WorkspaceFixture.get())
                .when(workspaceService)
                .workspace(anyInt());
        doReturn(ofNullable(WorkplaceFixture.get()))
                .when(workplaceRepository)
                .findByIdAndWorkspace(anyInt(), any(Workspace.class));
    }


    @BeforeEach
    public void setUp() {
    }


    @Test
    void workplaceBookingsOnDate_whenOk_shouldWork() throws Exception {
        Integer headquartersId = 2;
        LocalDate onDate = LocalDate.now();

        var user = UserFixture.getHeadquartersReceptionist();
        mockGetPrincipal(user);

        Headquarters headquarters = HeadquartersFixture.get();
        var principalHqId = WorkplaceBooking.class.getSuperclass().getSuperclass().getSuperclass().getDeclaredField("id");
        principalHqId.setAccessible(true);
        principalHqId.set(user.getHeadquarters(), headquartersId);

        doReturn(HeadquartersFixture.get())
                .when(headquartersService)
                .headquarters(headquartersId);
        doReturn(List.of(WorkplaceBookingFixture.get()))
                .when(workplaceBookingRepository)
                .findAllByHeadquartersAndBookingDate(any(Headquarters.class), any(LocalDate.class));

        var result = workplaceBookingService.workplaceBookingsOnDate(headquartersId, onDate);

        assertEquals(1, result.size(), "wrong dimension");
    }

    @Test
    void workplaceBookingsOnDate_whenNotHisHeadquarters_shouldThrow() throws Exception {
        Integer headquartersId = 2;
        LocalDate onDate = LocalDate.now();

        var user = UserFixture.getHeadquartersReceptionist();
        mockGetPrincipal(user);

        Headquarters headquarters = HeadquartersFixture.get();
        var principalHqId = WorkplaceBooking.class.getSuperclass().getSuperclass().getSuperclass().getDeclaredField("id");
        principalHqId.setAccessible(true);
        principalHqId.set(user.getHeadquarters(), 23);

        assertThrows(AccessDeniedException.class, () -> workplaceBookingService.workplaceBookingsOnDate(headquartersId, onDate));
    }

    @Test
    void workplaceBookingsFromDate_shouldWork() {
        LocalDate fromDate = LocalDate.now().minusYears(3);

        mockGetPrincipal(UserFixture.getCompanyManager());
        doReturn(List.of(EventFixture.get()))
                .when(workplaceBookingRepository)
                .findAllByWorkerAndBookingDateGreaterThanEqual(any(User.class), eq(fromDate), any(Sort.class));

        var result = workplaceBookingService.workplaceBookingsFromDate(fromDate);

        assertEquals(1, result.size(), "return not coherent");
    }

    @Test
    void workersOn_shouldWork() {
        LocalDate onDate = LocalDate.now().plusDays(3);
        Headquarters headquarters = HeadquartersFixture.get();

        doReturn(Set.of(UserFixture.getCompanyManager(), UserFixture.getEmployee()))
                .when(workplaceBookingRepository)
                .findAllWorkersBookedByHeadquartersAndBookingDate(headquarters, onDate);

        var result = workplaceBookingService.workersOn(onDate, headquarters);

        assertEquals(2, result.size(), "wrong no. of bookings");
    }

    @Test
    void book_whenNotAvailable_shouldThrow() {
        Integer workspaceId = 3;
        Integer workplaceId = 4;
        WorkplaceBookingRequestDTO request = WorkplaceBookingRequestDTO.builder()
                .bookingDate(LocalDate.now().plusDays(2))
                .build();

        mockGetWorkplace();
        mockGetPrincipal(UserFixture.getCompanyManager());
        doReturn(1L)
                .when(workplaceBookingRepository)
                .countAllByWorkerAndBookingDate(any(User.class), any(LocalDate.class));

        assertThrows(WorkplaceBookingAlreadyExistsForWorkerException.class, () -> workplaceBookingService.book(workspaceId, workplaceId, request));
    }

    @Test
    void book_whenAlreadyBooked_shouldThrow() {
        Integer workspaceId = 3;
        Integer workplaceId = 4;
        WorkplaceBookingRequestDTO request = WorkplaceBookingRequestDTO.builder()
                .bookingDate(LocalDate.now().plusDays(2))
                .build();

        mockGetWorkplace();
        mockGetPrincipal(UserFixture.getCompanyManager());
        doReturn(0L)
                .when(workplaceBookingRepository)
                .countAllByWorkerAndBookingDate(any(User.class), any(LocalDate.class));
        doReturn(1L)
                .when(workplaceBookingRepository)
                .countAllByWorkplaceAndBookingDate(any(Workplace.class), any(LocalDate.class));

        assertThrows(WorkplaceNotAvailableForBookingException.class, () -> workplaceBookingService.book(workspaceId, workplaceId, request));
    }

    @Test
    void book_whenOk_shouldWork() {
        Integer workspaceId = 3;
        Integer workplaceId = 4;
        WorkplaceBookingRequestDTO request = WorkplaceBookingRequestDTO.builder()
                .bookingDate(LocalDate.now().plusDays(2))
                .build();

        mockGetWorkplace();
        mockGetPrincipal(UserFixture.getCompanyManager());
        doReturn(0L)
                .when(workplaceBookingRepository)
                .countAllByWorkerAndBookingDate(any(User.class), any(LocalDate.class));
        doReturn(0L)
                .when(workplaceBookingRepository)
                .countAllByWorkplaceAndBookingDate(any(Workplace.class), any(LocalDate.class));
        doReturn("message")
                .when(emailNotificationService)
                .buildMessage(anyString(), anyMap());
        doReturn(WorkplaceBookingFixture.getFuture())
                .when(workplaceBookingRepository)
                .save(any(WorkplaceBooking.class));
        doNothing().when(emailNotificationService).notify(any(), anyString(), anyString());
        doReturn(Pair.of(20L, 100L)).when(workplaceService).countAvailableOnTotalForHeadquarters(any(Headquarters.class), any(LocalDate.class));
        doNothing().when(headquartersService)
                .notifyObservers(any(Headquarters.class), eq(headquartersWorkplaceBookingsObserverService), any(HeadquartersWorkplaceBookingsObserverService.Context.class));

        var result = workplaceBookingService.book(workspaceId, workplaceId, request);
        assertEquals(request.getBookingDate(), result.getBookingDate(), "not matching the request");
        verify(headquartersService).notifyObservers(any(Headquarters.class), eq(headquartersWorkplaceBookingsObserverService), any(HeadquartersWorkplaceBookingsObserverService.Context.class));
    }

    @Test
    void setParticipation_whenNotFound_shouldThrow() {
        Integer workspaceId = 3;
        Integer workplaceId = 4;
        Integer bookingId = 20;

        mockGetWorkplace();
        mockGetPrincipal(UserFixture.getHeadquartersReceptionist());
        doNothing().when(workplaceBookingService)
                .checkHeadquartersReceptionistPrincipalScopeOrThrow(any(), any(HeadquartersReceptionistUser.class));
        doReturn(Optional.empty())
                .when(workplaceBookingRepository)
                .findByIdAndWorkplace(anyInt(), any(Workplace.class));

        assertThrows(WorkplaceBookingNotFoundException.class, () -> workplaceBookingService.setParticipation(workspaceId, workplaceId, bookingId));
    }

    @Test
    void setParticipation_whenNotToday_shouldThrow() {
        Integer workspaceId = 3;
        Integer workplaceId = 4;
        Integer bookingId = 20;

        mockGetWorkplace();
        mockGetPrincipal(UserFixture.getHeadquartersReceptionist());
        doNothing().when(workplaceBookingService)
                .checkHeadquartersReceptionistPrincipalScopeOrThrow(any(), any(HeadquartersReceptionistUser.class));
        doReturn(ofNullable(WorkplaceBookingFixture.getFuture()))
                .when(workplaceBookingRepository)
                .findByIdAndWorkplace(anyInt(), any(Workplace.class));

        assertThrows(WorkplaceBookingParticipationNotSettableException.class, () -> workplaceBookingService.setParticipation(workspaceId, workplaceId, bookingId));
    }

    @Test
    void setParticipation_whenOk_shouldWork() {
        Integer workspaceId = 3;
        Integer workplaceId = 4;
        Integer bookingId = 20;

        mockGetWorkplace();
        mockGetPrincipal(UserFixture.getHeadquartersReceptionist());
        doNothing().when(workplaceBookingService)
                .checkHeadquartersReceptionistPrincipalScopeOrThrow(any(), any(HeadquartersReceptionistUser.class));
        doReturn(ofNullable(WorkplaceBookingFixture.get()))
                .when(workplaceBookingRepository)
                .findByIdAndWorkplace(anyInt(), any(Workplace.class));
        doReturn(WorkplaceBookingFixture.get())
                .when(workplaceBookingRepository)
                .save(any(WorkplaceBooking.class));

        assertDoesNotThrow(() -> workplaceBookingService.setParticipation(workspaceId, workplaceId, bookingId));
        verify(workplaceBookingRepository).save(any(WorkplaceBooking.class));
    }

    @Test
    void cancel_whenNotFound_shouldThrowButIgnore() {
        Integer workspaceId = 3;
        Integer workplaceId = 4;
        Integer bookingId = 20;

        mockGetWorkplace();
        mockGetPrincipal(UserFixture.getCompanyManager());
        doReturn(Optional.empty())
                .when(workplaceBookingRepository)
                .findByIdAndWorkplace(anyInt(), any(Workplace.class));

        assertDoesNotThrow(() -> workplaceBookingService.cancel(workspaceId, workplaceId, bookingId));
    }

    @Test
    void cancel_whenNotHisBooking_shouldThrow() {
        Integer workspaceId = 3;
        Integer workplaceId = 4;
        Integer bookingId = 20;

        mockGetWorkplace();
        mockGetPrincipal(UserFixture.getEmployee());
        doReturn(ofNullable(WorkplaceBookingFixture.getFuture()))
                .when(workplaceBookingRepository)
                .findByIdAndWorkplace(anyInt(), any(Workplace.class));

        assertThrows(AccessDeniedException.class, () -> workplaceBookingService.cancel(workspaceId, workplaceId, bookingId));
    }

    @Test
    void cancel_whenPast_shouldThrow() {
        Integer workspaceId = 3;
        Integer workplaceId = 4;
        Integer bookingId = 20;

        mockGetWorkplace();
        mockGetPrincipal(UserFixture.getCompanyManager());
        doReturn(ofNullable(WorkplaceBookingFixture.getPast()))
                .when(workplaceBookingRepository)
                .findByIdAndWorkplace(anyInt(), any(Workplace.class));

        assertThrows(WorkplaceBookingCancellationNotValidException.class, () -> workplaceBookingService.cancel(workspaceId, workplaceId, bookingId));
    }

    @Test
    void cancel_whenFound_shouldWork() {
        Integer workspaceId = 3;
        Integer workplaceId = 4;
        Integer bookingId = 20;

        mockGetWorkplace();
        mockGetPrincipal(UserFixture.getCompanyManager());
        doReturn(ofNullable(WorkplaceBookingFixture.getFuture()))
                .when(workplaceBookingRepository)
                .findByIdAndWorkplace(anyInt(), any(Workplace.class));
        doNothing().when(workplaceBookingRepository).softDelete(any(WorkplaceBooking.class));

        assertDoesNotThrow(() -> workplaceBookingService.cancel(workspaceId, workplaceId, bookingId));
    }

    @Test
    void cancelAll_() {
        Workplace workplace = WorkplaceFixture.get();

        doReturn(Set.of(WorkplaceBookingFixture.get()))
                .when(workplaceBookingRepository)
                .findAllByWorkplace(workplace);

        workplaceBookingService.cancelAll(workplace);

        verify(workplaceBookingRepository).softDelete(any(WorkplaceBooking.class));
    }

}
