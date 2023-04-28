package com.dp.spring.parallel._startup;

import com.dp.spring.parallel.hestia.database.entities.AdminUser;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AdminUserLoadingRunner implements CommandLineRunner {
    private static final String EMPTY_FIELD = "";
    private static final LocalDate ADMIN_BIRTHDATE = LocalDate.of(1970, 1, 1);
    private static final String ADMIN_FIRST_NAME = "admin";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private final UserRepository<AdminUser> userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (this.userRepository.count() == 0) {
            AdminUser startupAdmin = AdminUser.builder()
                    .firstName(ADMIN_FIRST_NAME)
                    .lastName(EMPTY_FIELD)
                    .birthDate(ADMIN_BIRTHDATE)
                    .phoneNumber(EMPTY_FIELD)
                    .city(EMPTY_FIELD)
                    .address(EMPTY_FIELD)
                    .email(ADMIN_USERNAME)
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .build();
            this.userRepository.save(startupAdmin);
        }
    }
}
