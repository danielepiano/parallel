package com.dp.spring.parallel.hestia.api.controllers;

import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.enums.UserRole;
import com.dp.spring.parallel.hestia.database.repositories.AdminUserRepository;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/demo")
@RequiredArgsConstructor
@Transactional
public class _DemoController {
    private final UserRepository userRepository;
    private final AdminUserRepository adminUserRepository;

    @GetMapping
    @Secured(UserRole.Constants.ROLE_ADMIN_VALUE)
    public Map<String, Collection<? extends User>> hello() {
        return Map.of("all", userRepository.findAll(),
                "admin", adminUserRepository.findAll()
        );
    }

    @DeleteMapping
    public void delete() {
        this.userRepository.softDeleteById(1);
    }
}
