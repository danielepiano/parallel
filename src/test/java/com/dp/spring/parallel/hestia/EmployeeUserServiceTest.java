package com.dp.spring.parallel.hestia;

import com.dp.spring.parallel.common.fixtures.CompanyFixture;
import com.dp.spring.parallel.common.fixtures.UserFixture;
import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.services.CompanyService;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.repositories.EmployeeUserRepository;
import com.dp.spring.parallel.hestia.services.impl.EmployeeUserServiceImpl;
import com.dp.spring.parallel.hestia.services.registration_strategies.EmployeeRegistrationStrategy;
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
class EmployeeUserServiceTest {

    @Spy
    EmployeeRegistrationStrategy employeeRegistrationStrategy;

    @Spy
    CompanyService companyService;

    @Spy
    EmployeeUserRepository employeeUserRepository;

    @InjectMocks
    @Spy
    EmployeeUserServiceImpl employeeUserService;


    void mockCheckPrincipalScope() {
        doNothing().when(employeeUserService).checkPrincipalScopeOrThrow(any());
    }


    @BeforeEach
    public void setUp() {
    }


    @Test
    void register_shouldCallSuper() {
        Integer companyId = 1;
        RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                .firstName("employee")
                .lastName("employee")
                .email("employee")
                .birthDate(LocalDate.now())
                .phoneNumber("number")
                .city("city")
                .address("address")
                .jobPosition("employee")
                .build();

        doNothing().when(companyService).checkExistence(anyInt());
        mockCheckPrincipalScope();
        doNothing().when(employeeUserService).register(anyInt(), any(RegistrationRequestDTO.class), eq(employeeRegistrationStrategy));

        employeeUserService.register(companyId, request);

        verify(employeeUserService).register(anyInt(), any(RegistrationRequestDTO.class), eq(employeeRegistrationStrategy));
    }

    @Test
    void employeesFor_shouldWork() {
        Company company = CompanyFixture.get();

        doReturn(Set.of(UserFixture.getCompanyManager()))
                .when(employeeUserRepository)
                .findAllByCompany(any(Company.class));

        employeeUserService.employeesFor(company);

        verify(employeeUserRepository).findAllByCompany(any(Company.class));
    }

    @Test
    void disable_shouldWork() {
        Integer companyId = 1;
        Integer employeeId = 3;

        doNothing().when(companyService).checkExistence(anyInt());
        mockCheckPrincipalScope();
        doNothing().when(employeeUserRepository).softDeleteById(anyInt());

        employeeUserService.disable(companyId, employeeId);

        verify(employeeUserRepository).softDeleteById(anyInt());
    }

    @Test
    void disableEmployeesFor_shouldWork() {
        Company company = CompanyFixture.get();

        doReturn(Set.of(UserFixture.getEmployee()))
                .when(employeeUserService)
                .employeesFor(any(Company.class));

        employeeUserService.disableEmployeesFor(company);

        verify(employeeUserRepository).deleteAll(anySet());
    }

}
