package com.dp.spring.parallel.common.fixtures;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hestia.database.entities.AdminUser;
import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;
import com.dp.spring.parallel.hestia.database.entities.EmployeeUser;
import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.parallel.hestia.database.enums.UserRole;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserFixture {

    public static AdminUser getAdmin() {
        return AdminUser.builder()
                .id(1)
                .firstName("admin")
                .lastName("admin")
                .email("admin")
                .password(new BCryptPasswordEncoder().encode("password"))
                .birthDate(LocalDate.now())
                .phoneNumber("number")
                .city("city")
                .address("address")
                .role(UserRole.ADMIN)
                .build();
    }

    public static CompanyManagerUser getCompanyManager() {
        List<Headquarters> favHq = new ArrayList<>();
        favHq.add(HeadquartersFixture.get());
        return CompanyManagerUser.builder()
                .id(2)
                .firstName("company-manager")
                .lastName("company-manager")
                .email("company-manager")
                .password(new BCryptPasswordEncoder().encode("password"))
                .birthDate(LocalDate.now())
                .phoneNumber("number")
                .city("city")
                .address("address")
                .role(UserRole.COMPANY_MANAGER)
                .company(CompanyFixture.get())
                .jobPosition("company-manager")
                .favoriteHeadquarters(favHq)
                .build();
    }

    public static EmployeeUser getEmployee() {
        List<Headquarters> favHq = new ArrayList<>();
        favHq.add(HeadquartersFixture.get());
        return EmployeeUser.builder()
                .id(3)
                .firstName("employee")
                .lastName("employee")
                .email("employee")
                .password(new BCryptPasswordEncoder().encode("password"))
                .birthDate(LocalDate.now())
                .phoneNumber("number")
                .city("city")
                .address("address")
                .role(UserRole.EMPLOYEE)
                .company(CompanyFixture.get())
                .jobPosition("employee")
                .favoriteHeadquarters(favHq)
                .build();
    }

    public static HeadquartersReceptionistUser getHeadquartersReceptionist() {
        return HeadquartersReceptionistUser.builder()
                .id(4)
                .firstName("receptionist")
                .lastName("receptionist")
                .email("receptionist")
                .password(new BCryptPasswordEncoder().encode("password"))
                .birthDate(LocalDate.now())
                .phoneNumber("number")
                .city("city")
                .address("address")
                .role(UserRole.HEADQUARTERS_RECEPTIONIST)
                .headquarters(HeadquartersFixture.get())
                .build();
    }

}
