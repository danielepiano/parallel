package com.dp.spring.parallel.hestia.api.controllers;

import com.dp.spring.parallel.common.utils.ResourcesUtils;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.enums.UserRole;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import com.dp.spring.parallel.mnemosyne.utils.EmailMessageParser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/demo")
@RequiredArgsConstructor
@Transactional
public class _DemoController {
    private final UserRepository<User> userRepository;

    @GetMapping
    @Secured(UserRole.Constants.ROLE_ADMIN_VALUE)
    public String /*List<Pair<Integer, UserRole>>*/ hello() throws IOException {
        String message = ResourcesUtils.readFileAsString("email/first-access-credentials-template.html");
        return EmailMessageParser.parse(message, Map.of(EmailMessageParser.Keyword.FIRST_NAME, "Daniele"));
        /*return this.userRepository.findAll()
                .stream()
                .map(x -> Pair.of(x.getId(), x.getRole()))
                .toList();*/
    }

    @DeleteMapping
    public void delete() {
        this.userRepository.softDeleteById(1);
    }
}
