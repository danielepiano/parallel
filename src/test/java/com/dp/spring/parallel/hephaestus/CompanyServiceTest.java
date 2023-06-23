package com.dp.spring.parallel.hephaestus;

import com.dp.spring.parallel.common.exceptions.CompanyAlreadyExistsException;
import com.dp.spring.parallel.common.exceptions.CompanyNotFoundException;
import com.dp.spring.parallel.common.fixtures.CompanyFixture;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateCompanyRequestDTO;
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
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    HeadquartersService headquartersService;
    @Mock
    CompanyManagerUserService companyManagerUserService;
    @Mock
    EmployeeUserService employeeUserService;

    @Spy
    CompanyRepository companyRepository;

    @InjectMocks
    @Spy
    CompanyServiceImpl companyService;


    // Mock
    void mockLoggedUser(User user) {
        doReturn(user)
                .when(companyService)
                .getPrincipalOrThrow();
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

        companyService.company(companyId);
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

}
