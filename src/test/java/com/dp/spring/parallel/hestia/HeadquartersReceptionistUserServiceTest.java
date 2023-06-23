package com.dp.spring.parallel.hestia;

import com.dp.spring.parallel.common.fixtures.HeadquartersFixture;
import com.dp.spring.parallel.common.fixtures.UserFixture;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.repositories.HeadquartersReceptionistUserRepository;
import com.dp.spring.parallel.hestia.services.impl.HeadquartersReceptionistUserServiceImpl;
import com.dp.spring.parallel.hestia.services.registration_strategies.HeadquartersReceptionistRegistrationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HeadquartersReceptionistUserServiceTest {

    @Spy
    HeadquartersReceptionistRegistrationStrategy headquartersReceptionistRegistrationStrategy;

    @Spy
    HeadquartersService headquartersService;

    @Spy
    HeadquartersReceptionistUserRepository headquartersReceptionistUserRepository;

    @InjectMocks
    @Spy
    HeadquartersReceptionistUserServiceImpl headquartersReceptionistUserService;


    void mockCheckPrincipalScope() {
        doNothing().when(headquartersReceptionistUserService).checkPrincipalScopeOrThrow(any());
    }


    @BeforeEach
    public void setUp() {
    }


    @Test
    void register_shouldCallSuper() {
        Integer companyId = 1;
        Integer headquartersId = 2;
        RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                .firstName("employee")
                .lastName("employee")
                .email("employee")
                .birthDate(LocalDate.now())
                .phoneNumber("number")
                .city("city")
                .address("address")
                .build();

        doNothing().when(headquartersService).checkExistence(anyInt(), anyInt());
        mockCheckPrincipalScope();
        doNothing().when(headquartersReceptionistUserService).register(anyInt(), any(RegistrationRequestDTO.class), eq(headquartersReceptionistRegistrationStrategy));

        headquartersReceptionistUserService.register(companyId, headquartersId, request);

        verify(headquartersReceptionistUserService).register(anyInt(), any(RegistrationRequestDTO.class), eq(headquartersReceptionistRegistrationStrategy));
    }

    @Test
    void headquartersReceptionistsFor_shouldWork() {
        Headquarters headquarters = HeadquartersFixture.get();

        doReturn(Set.of(UserFixture.getHeadquartersReceptionist()))
                .when(headquartersReceptionistUserRepository)
                .findAllByHeadquarters(any(Headquarters.class));

        headquartersReceptionistUserService.headquartersReceptionistsFor(headquarters);

        verify(headquartersReceptionistUserRepository).findAllByHeadquarters(any(Headquarters.class));
    }

    @Test
    void disable_shouldWork() {
        Integer companyId = 1;
        Integer headquartersId = 2;
        Integer employeeId = 3;

        doNothing().when(headquartersService).checkExistence(anyInt(), anyInt());
        mockCheckPrincipalScope();
        doNothing().when(headquartersReceptionistUserRepository).softDeleteById(anyInt());

        headquartersReceptionistUserService.disable(companyId, headquartersId, employeeId);

        verify(headquartersReceptionistUserRepository).softDeleteById(anyInt());
    }

}
