package com.dp.spring.parallel.hestia;

import com.dp.spring.parallel.common.fixtures.CompanyFixture;
import com.dp.spring.parallel.common.fixtures.UserFixture;
import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.services.CompanyService;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.repositories.CompanyManagerUserRepository;
import com.dp.spring.parallel.hestia.services.impl.CompanyManagerUserServiceImpl;
import com.dp.spring.parallel.hestia.services.registration_strategies.CompanyManagerRegistrationStrategy;
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
class CompanyManagerUserServiceTest {

    @Spy
    CompanyManagerRegistrationStrategy companyManagerRegistrationStrategy;

    @Spy
    CompanyService companyService;

    @Spy
    CompanyManagerUserRepository companyManagerUserRepository;

    @InjectMocks
    @Spy
    CompanyManagerUserServiceImpl companyManagerUserService;


    void mockCheckPrincipalScope() {
        doNothing().when(companyManagerUserService).checkPrincipalScopeOrThrow(any());
    }


    @BeforeEach
    public void setUp() {
    }


    @Test
    void register_shouldCallSuper() {
        Integer companyId = 1;
        RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                .firstName("company-manager")
                .lastName("company-manager")
                .email("company-manager")
                .birthDate(LocalDate.now())
                .phoneNumber("number")
                .city("city")
                .address("address")
                .jobPosition("company-manager")
                .build();

        doNothing().when(companyService).checkExistence(anyInt());
        mockCheckPrincipalScope();
        doNothing().when(companyManagerUserService).register(anyInt(), any(RegistrationRequestDTO.class), eq(companyManagerRegistrationStrategy));

        companyManagerUserService.register(companyId, request);

        verify(companyManagerUserService).register(anyInt(), any(RegistrationRequestDTO.class), eq(companyManagerRegistrationStrategy));
    }

    @Test
    void companyManagersFor_shouldWork() {
        Company company = CompanyFixture.get();

        doReturn(Set.of(UserFixture.getCompanyManager()))
                .when(companyManagerUserRepository)
                .findAllByCompany(any(Company.class));

        companyManagerUserService.companyManagersFor(company);

        verify(companyManagerUserRepository).findAllByCompany(any(Company.class));
    }

    @Test
    void disable_shouldWork() {
        Integer companyId = 1;
        Integer companyManagerId = 2;

        doNothing().when(companyService).checkExistence(anyInt());
        mockCheckPrincipalScope();
        doNothing().when(companyManagerUserRepository).softDeleteById(anyInt());

        companyManagerUserService.disable(companyId, companyManagerId);

        verify(companyManagerUserRepository).softDeleteById(anyInt());
    }

}
