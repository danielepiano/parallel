package com.dp.spring.parallel.common.services;

import com.dp.spring.parallel.common.exceptions.CompanyNotFoundException;
import com.dp.spring.parallel.common.exceptions.HeadquartersNotFoundException;
import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.repositories.CompanyRepository;
import com.dp.spring.parallel.hephaestus.database.repositories.HeadquartersRepository;
import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;
import com.dp.spring.parallel.hestia.database.entities.EmployeeUser;
import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.springcore.exceptions.BaseExceptionConstants;
import com.dp.spring.springcore.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static java.util.Optional.ofNullable;

@Service
public abstract class BusinessService {
    @Autowired
    protected CompanyRepository companyRepository;
    @Autowired
    protected HeadquartersRepository headquartersRepository;


    /**
     * Compact method for getting a resource by only its id, otherwise returning a {@link ResourceNotFoundException}.
     *
     * @param id                        the id of the resource to get
     * @param repository                the repository to check the resource from
     * @param resourceNotFoundException the exception to throw in case resource is not found
     * @param <E>                       the type of the resource entity
     * @param <ID>                      the type of the resource id
     * @param <R>                       the type of the repository
     * @param <X>                       the type of the exception to throw
     * @return the resource
     */
    public <E, ID, R extends CrudRepository<E, ID>, X extends ResourceNotFoundException>
    E getResourceOrThrow(final ID id, final R repository, final X resourceNotFoundException) {
        return repository.findById(id)
                .orElseThrow(() -> resourceNotFoundException);
    }

    /**
     * Common {@link #getResourceOrThrow(Object, CrudRepository, ResourceNotFoundException)} implementation for a
     * {@link Company} resource.
     *
     * @param companyId the id of the company to get
     * @return the company
     */
    public Company getCompanyOrThrow(final Integer companyId) {
        return this.getResourceOrThrow(companyId, companyRepository, new CompanyNotFoundException(companyId));
    }

    /**
     * Common method for getting a {@link Headquarters} resource by its id and the id of the related company,
     * otherwise returning a {@link ResourceNotFoundException}.
     *
     * @param headquartersId the id of the headquarters to get
     * @param companyId      the company id of the headquarters to get
     * @return the company
     */
    public Headquarters getHeadquartersOrThrow(final Integer headquartersId, final Integer companyId) {
        return headquartersRepository.findByIdAndCompany(headquartersId, this.getCompanyOrThrow(companyId))
                .orElseThrow(() -> new HeadquartersNotFoundException(headquartersId));
    }


    /**
     * Compact method for checking the existence of a resource by only its id, otherwise returning a
     * {@link ResourceNotFoundException}.
     *
     * @param id                        the id of the resource to check
     * @param repository                the repository to check the resource from
     * @param resourceNotFoundException the exception to throw in case resource is not found
     * @param <ID>                      the type of the resource id
     * @param <R>                       the type of the repository
     * @param <X>                       the type of the exception to throw
     */
    public <ID, R extends CrudRepository<?, ID>, X extends ResourceNotFoundException>
    void checkResourceExistenceByIdOrThrow(final ID id, final R repository, final X resourceNotFoundException) {
        if (!repository.existsById(id)) {
            throw resourceNotFoundException;
        }
    }

    /**
     * Common {@link #checkResourceExistenceByIdOrThrow(Object, CrudRepository, ResourceNotFoundException)}
     * implementation for a {@link Company} resource.
     *
     * @param companyId the id of the company to check
     */
    public void checkCompanyExistenceOrThrow(final Integer companyId) {
        this.checkResourceExistenceByIdOrThrow(companyId, companyRepository, new CompanyNotFoundException(companyId));
    }

    /**
     * Common method for checkig the existence of a {@link Headquarters} resource by its id and the id of the related
     * company, otherwise returning a {@link ResourceNotFoundException}.
     *
     * @param headquartersId the id of the headquarters to get
     * @param companyId      the company id of the headquarters to check
     */
    public void checkHeadquartersExistenceOrThrow(final Integer headquartersId, final Integer companyId) {
        if (!headquartersRepository.existsByIdAndCompanyId(headquartersId, companyId)) {
            throw new HeadquartersNotFoundException(headquartersId);
        }
    }


    public User getPrincipalOrThrow() {
        return ofNullable(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .map(User.class::cast)
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

    // @todo company in realtà???
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
