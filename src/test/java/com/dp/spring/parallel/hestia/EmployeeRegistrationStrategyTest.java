package com.dp.spring.parallel.hestia;

import com.dp.spring.parallel.common.fixtures.CompanyFixture;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateCompanyRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateCompanyRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.services.CompanyService;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.EmployeeUser;
import com.dp.spring.parallel.hestia.services.registration_strategies.EmployeeRegistrationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class EmployeeRegistrationStrategyTest {

    @Spy
    EmployeeRegistrationStrategy employeeRegistrationStrategy;

    @Spy
    CompanyService companyService = new CompanyService() {
        @Override
        public Company add(CreateCompanyRequestDTO toAddData) {
            return null;
        }

        @Override
        public Company company(Integer companyId) {
            return null;
        }

        @Override
        public Set<Company> companies() {
            return null;
        }

        @Override
        public Company update(Integer companyId, UpdateCompanyRequestDTO updatedData) {
            return null;
        }

        @Override
        public void remove(Integer companyId) {

        }

        @Override
        public void checkExistence(Integer companyId) {

        }
    };


    @BeforeEach
    public void setUp() throws Exception {
        var constructor = EmployeeRegistrationStrategy.class
                .getDeclaredConstructor();
        constructor.setAccessible(true);

        employeeRegistrationStrategy = constructor.newInstance();

        var companyService = EmployeeRegistrationStrategy.class.getDeclaredField("companyService");
        companyService.setAccessible(true);
        companyService.set(employeeRegistrationStrategy, this.companyService);
    }

    @Test
    void buildUser_shouldMap() throws Exception {
        String encodedPassword = "encoded-password";
        Integer scopeId = 1;
        RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                .firstName("admin")
                .lastName("admin")
                .email("admin")
                .birthDate(LocalDate.now())
                .phoneNumber("number")
                .city("city")
                .address("address")
                .jobPosition("company-manager")
                .build();

        Company company = CompanyFixture.get();
        doReturn(company).when(companyService).company(scopeId);

        var buildUser = EmployeeRegistrationStrategy.class
                .getDeclaredMethod("buildUser", String.class, Integer.class, RegistrationRequestDTO.class);
        buildUser.setAccessible(true);
        EmployeeUser result = (EmployeeUser) buildUser.invoke(employeeRegistrationStrategy, encodedPassword, scopeId, request);

        assertEquals(request.getFirstName(), result.getFirstName(), "wrong mapping");
        assertEquals(request.getLastName(), result.getLastName(), "wrong mapping");
        assertEquals(request.getBirthDate(), result.getBirthDate(), "wrong mapping");
        assertEquals(request.getPhoneNumber(), result.getPhoneNumber(), "wrong mapping");
        assertEquals(request.getCity(), result.getCity(), "wrong mapping");
        assertEquals(request.getAddress(), result.getAddress(), "wrong mapping");
        assertEquals(request.getEmail(), result.getEmail(), "wrong mapping");
        assertEquals(encodedPassword, result.getPassword(), "wrong mapping");
        assertEquals(company, result.getCompany(), "wrong mapping");
        assertEquals(request.getJobPosition(), result.getJobPosition(), "wrong mapping");
    }

}
