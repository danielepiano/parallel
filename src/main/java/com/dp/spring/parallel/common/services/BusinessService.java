package com.dp.spring.parallel.common.services;

import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;
import com.dp.spring.parallel.hestia.database.entities.EmployeeUser;
import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import com.dp.spring.springcore.exceptions.BaseExceptionConstants;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static java.util.Optional.ofNullable;

@Service
public abstract class BusinessService {
    @Autowired
    private UserRepository userRepository;


    /**
     * Retrieving the logged user.
     *
     * @return the logged user
     */
    @Transactional
    public User getPrincipalOrThrow() {
        return ofNullable(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .map(User.class::cast)
                .flatMap(principal -> this.userRepository.findById(principal.getId()))
                .orElseThrow(() -> new AccessDeniedException(BaseExceptionConstants.ACCESS_DENIED.getDetail()));
    }

    /**
     * Checking if a given company id matches the id of the company attached to the principal.
     *
     * @param companyId the id of the company to check
     */
    public void checkPrincipalScopeOrThrow(Integer companyId) {
        User principal = getPrincipalOrThrow();

        switch (principal.getRole()) {
            case ADMIN:
                break;
            case COMPANY_MANAGER:
                this.checkCompanyManagerPrincipalScopeOrThrow(companyId, (CompanyManagerUser) principal);
                break;
            case HEADQUARTERS_RECEPTIONIST:
                this.checkHeadquartersReceptionistPrincipalScopeOrThrow(companyId, (HeadquartersReceptionistUser) principal);
                break;
            case EMPLOYEE:
                this.checkEmployeePrincipalScopeOrThrow(companyId, (EmployeeUser) principal);
                break;
            default:
                throw new AccessDeniedException(BaseExceptionConstants.ACCESS_DENIED.getDetail());
        }
    }

    public void checkCompanyManagerPrincipalScopeOrThrow(Integer companyId, CompanyManagerUser principal) {
        if (!Objects.equals(companyId, principal.getCompany().getId())) {
            throw new AccessDeniedException(BaseExceptionConstants.ACCESS_DENIED.getDetail());
        }
    }

    public void checkHeadquartersReceptionistPrincipalScopeOrThrow(Integer companyId, HeadquartersReceptionistUser principal) {
        if (!Objects.equals(companyId, principal.getHeadquarters().getCompany().getId())) {
            throw new AccessDeniedException(BaseExceptionConstants.ACCESS_DENIED.getDetail());
        }
    }

    public void checkEmployeePrincipalScopeOrThrow(Integer companyId, EmployeeUser principal) {
        if (!Objects.equals(companyId, principal.getCompany().getId())) {
            throw new AccessDeniedException(BaseExceptionConstants.ACCESS_DENIED.getDetail());
        }
    }
}
