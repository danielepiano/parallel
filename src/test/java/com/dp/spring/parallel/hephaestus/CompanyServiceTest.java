package com.dp.spring.parallel.hephaestus;

import com.dp.spring.parallel.common.exceptions.CompanyAlreadyExistsException;
import com.dp.spring.parallel.common.exceptions.CompanyNotDeletableException;
import com.dp.spring.parallel.common.exceptions.CompanyNotFoundException;
import com.dp.spring.parallel.common.fixtures.CompanyFixture;
import com.dp.spring.parallel.common.fixtures.UserFixture;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateCompanyRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateCompanyRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.repositories.CompanyRepository;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.hephaestus.services.impl.CompanyServiceImpl;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.services.CompanyManagerUserService;
import com.dp.spring.parallel.hestia.services.EmployeeUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Spy
    HeadquartersService headquartersService;
    @Spy
    CompanyManagerUserService companyManagerUserService;
    @Spy
    EmployeeUserService employeeUserService;

    @Spy
    CompanyRepository companyRepository;

    @InjectMocks
    @Spy
    CompanyServiceImpl companyService;


    // Mock
    void mockGetPrincipal(User user) {
        doReturn(user)
                .when(companyService)
                .getPrincipalOrThrow();
    }

    void mockCheckPrincipalScope() {
        doNothing().when(companyService).checkPrincipalScopeOrThrow(anyInt());
    }

    @BeforeEach
    public void setUp() {
    }


    @Test
    void add_whenConflict_shouldThrow() {
        CreateCompanyRequestDTO request = CreateCompanyRequestDTO.builder()
                .name("company-test")
                .description("description-test")
                .city("city-test")
                .address("address-test")
                .phoneNumber("number-test")
                .websiteUrl("url-test")
                .build();

        doReturn(true)
                .when(companyRepository)
                .existsByNameAndCityAndAddress(anyString(), anyString(), anyString());

        assertThrows(CompanyAlreadyExistsException.class, () -> companyService.add(request));
    }

    @Test
    void add_whenOk_shouldWork() {
        CreateCompanyRequestDTO request = CreateCompanyRequestDTO.builder()
                .name("company-test")
                .description("description-test")
                .city("city-test")
                .address("address-test")
                .phoneNumber("number-test")
                .websiteUrl("url-test")
                .build();

        doReturn(false)
                .when(companyRepository)
                .existsByNameAndCityAndAddress(anyString(), anyString(), anyString());

        companyService.add(request);

        verify(companyRepository).save(any(Company.class));
    }

    @Test
    void company_whenExists_shouldWork() {
        Integer companyId = 1;

        doReturn(ofNullable(CompanyFixture.get()))
                .when(companyRepository)
                .findById(anyInt());

        assertDoesNotThrow(() -> companyService.company(companyId));
    }

    @Test
    void company_whenNotFound_shouldThrow() {
        Integer companyId = 1;

        doReturn(Optional.empty())
                .when(companyRepository)
                .findById(anyInt());

        assertThrows(CompanyNotFoundException.class, () -> companyService.company(companyId));
    }

    @Test
    void companies_when1_shouldWork() {
        doReturn(List.of(CompanyFixture.get()))
                .when(companyRepository)
                .findAll();

        Set<Company> companies = companyService.companies();
        assertEquals(1, companies.size(), "wrong size");
    }

    @Test
    void update_whenNotFound_shouldThrow() {
        Integer companyId = 1;
        UpdateCompanyRequestDTO request = UpdateCompanyRequestDTO.builder()
                .name("company-test")
                .description("description-test")
                .city("city-test")
                .address("address-test")
                .phoneNumber("number-test")
                .websiteUrl("url-test")
                .build();

        doReturn(Optional.empty())
                .when(companyRepository)
                .findById(anyInt());

        assertThrows(CompanyNotFoundException.class, () -> companyService.update(companyId, request));
    }

    @Test
    void update_whenConflict_shouldThrow() {
        Integer companyId = 1;
        UpdateCompanyRequestDTO request = UpdateCompanyRequestDTO.builder()
                .name("company-test")
                .description("description-test")
                .city("city-test")
                .address("address-test")
                .phoneNumber("number-test")
                .websiteUrl("url-test")
                .build();

        doReturn(ofNullable(CompanyFixture.get()))
                .when(companyRepository)
                .findById(anyInt());
        mockCheckPrincipalScope();
        doReturn(true)
                .when(companyRepository)
                .existsByIdNotAndNameAndCityAndAddress(anyInt(), anyString(), anyString(), anyString());

        assertThrows(CompanyAlreadyExistsException.class, () -> companyService.update(companyId, request));
    }

    @Test
    void update_whenOk_shouldWork() {
        Integer companyId = 1;
        UpdateCompanyRequestDTO request = UpdateCompanyRequestDTO.builder()
                .name("company-test")
                .description("description-test")
                .city("city-test")
                .address("address-test")
                .phoneNumber("number-test")
                .websiteUrl("url-test")
                .build();

        doReturn(ofNullable(CompanyFixture.get()))
                .when(companyRepository)
                .findById(anyInt());
        mockCheckPrincipalScope();
        doReturn(false)
                .when(companyRepository)
                .existsByIdNotAndNameAndCityAndAddress(anyInt(), anyString(), anyString(), anyString());

        companyService.update(companyId, request);

        verify(companyRepository).save(any(Company.class));
    }

    @Test
    void return_whenNotFound_shouldThrow() {
        Integer companyId = 1;

        doReturn(Optional.empty())
                .when(companyRepository)
                .findById(anyInt());

        assertThrows(CompanyNotFoundException.class, () -> companyService.remove(companyId));
    }

    @Test
    void remove_whenNotDeletable_shouldThrow() {
        Integer companyId = 1;

        doReturn(ofNullable(CompanyFixture.get()))
                .when(companyRepository)
                .findById(anyInt());
        mockCheckPrincipalScope();
        doReturn(Set.of(UserFixture.getCompanyManager()))
                .when(companyManagerUserService)
                .companyManagersFor(any(Company.class));

        assertThrows(CompanyNotDeletableException.class, () -> companyService.remove(companyId));
    }

    @Test
    void remove_whenOk_shouldWork() {
        Integer companyId = 1;

        doReturn(ofNullable(CompanyFixture.get()))
                .when(companyRepository)
                .findById(anyInt());
        mockCheckPrincipalScope();
        doReturn(emptySet())
                .when(companyManagerUserService)
                .companyManagersFor(any(Company.class));

        companyService.remove(companyId);

        verify(headquartersService).removeAll(any(Company.class));
        verify(employeeUserService).disableEmployeesFor(any(Company.class));
        verify(companyRepository).softDelete(any(Company.class));
    }

    @Test
    void checkExistence_whenNotFound_shouldThrow() {
        Integer companyId = 1;

        doReturn(false)
                .when(companyRepository)
                .existsById(anyInt());

        assertThrows(CompanyNotFoundException.class, () -> companyService.checkExistence(companyId));
    }

    @Test
    void checkExistence_whenFound_shouldWork() {
        Integer companyId = 1;

        doReturn(true)
                .when(companyRepository)
                .existsById(anyInt());

        assertDoesNotThrow(() -> companyService.checkExistence(companyId));
    }

}
