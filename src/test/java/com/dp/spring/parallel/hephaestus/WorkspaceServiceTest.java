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
import com.dp.spring.parallel.hephaestus.database.repositories.WorkspaceRepository;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.hephaestus.services.WorkplaceService;
import com.dp.spring.parallel.hephaestus.services.impl.CompanyServiceImpl;
import com.dp.spring.parallel.hephaestus.services.impl.WorkspaceServiceImpl;
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
class WorkspaceServiceTest {

    @Spy
    WorkplaceService workplaceService;
    @Spy
    HeadquartersService headquartersService;

    @Spy
    WorkspaceRepository workspaceRepository;

    @InjectMocks
    @Spy
    WorkspaceServiceImpl workspaceService;


    // Mock
    void mockGetPrincipal(User user) {
        doReturn(user)
                .when(workspaceService)
                .getPrincipalOrThrow();
    }

    void mockCheckPrincipalScope() {
        doNothing().when(workspaceService).checkPrincipalScopeOrThrow(anyInt());
    }

    @BeforeEach
    public void setUp() {
    }

    @Test
    void add_when_should() {

    }

}
