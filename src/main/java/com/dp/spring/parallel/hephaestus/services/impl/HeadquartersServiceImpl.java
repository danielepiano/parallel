package com.dp.spring.parallel.hephaestus.services.impl;

import com.dp.spring.parallel.common.exceptions.HeadquartersAlreadyExistsException;
import com.dp.spring.parallel.common.exceptions.HeadquartersNotDeletableException;
import com.dp.spring.parallel.common.exceptions.HeadquartersNotFoundException;
import com.dp.spring.parallel.common.services.BusinessService;
import com.dp.spring.parallel.hephaestus.api.dtos.CompanyHeadquartersResponseDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateHeadquartersRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.HeadquartersResponseDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateHeadquartersRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.hephaestus.services.WorkspaceService;
import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.parallel.hestia.services.HeadquartersReceptionistUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Headquarters operations service implementation.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class HeadquartersServiceImpl extends BusinessService implements HeadquartersService {
    private final HeadquartersReceptionistUserService headquartersReceptionistUserService;
    private final WorkspaceService workspaceService;


    /**
     * Adding a headquarters.
     *
     * @param companyId the id of the company of the headquarters to add
     * @param toAddData the data of the headquarters to add
     */
    @Override
    public void add(Integer companyId, CreateHeadquartersRequestDTO toAddData) {
        final Company company = super.getCompanyOrThrow(companyId);
        super.checkPrincipalScopeOrThrow(companyId);

        this.checkHeadquartersUniquenessOrThrow(toAddData.getCity(), toAddData.getAddress(), company);

        final Headquarters toAdd = new Headquarters()
                .setCity(toAddData.getCity())
                .setAddress(toAddData.getAddress())
                .setPhoneNumber(toAddData.getPhoneNumber())
                .setFeDescription(toAddData.getFeDescription())
                .setCompany(company);

        super.headquartersRepository.save(toAdd);
    }

    /**
     * Retrieving a headquarters given its id.
     *
     * @param headquartersId the id of the headquarters to retrieve
     * @return the headquarters
     */
    @Override
    public HeadquartersResponseDTO headquarters(Integer headquartersId) {
        final Headquarters headquarters = super.getResourceOrThrow(
                headquartersId,
                super.headquartersRepository,
                new HeadquartersNotFoundException(headquartersId)
        );
        return HeadquartersResponseDTO.of(headquarters);
    }

    /**
     * Retrieving any headquarters of any company.
     *
     * @return the headquarters
     */
    @Override
    public Set<HeadquartersResponseDTO> headquarters() {
        return super.headquartersRepository.findAll().stream()
                .map(HeadquartersResponseDTO::of)
                .filter(Objects::nonNull)
                .collect(toSet());
    }

    /**
     * Retrieving the headquarters of a given company.
     *
     * @param companyId the id of the company
     * @return the headquarters of the company
     */
    @Override
    public Set<CompanyHeadquartersResponseDTO> companyHeadquarters(Integer companyId) {
        return super.headquartersRepository.findAllByCompany(super.getCompanyOrThrow(companyId)).stream()
                .map(CompanyHeadquartersResponseDTO::of)
                .filter(Objects::nonNull)
                .collect(toSet());
    }

    /**
     * Updating a headquarters.
     *
     * @param companyId      the id of the company of the headquarters to update
     * @param headquartersId the id of the headquarters to update
     * @param updatedData    the new data of the headquarters to update
     */
    @Override
    public void update(Integer companyId, Integer headquartersId, UpdateHeadquartersRequestDTO updatedData) {
        final Headquarters toUpdate = super.getHeadquartersOrThrow(headquartersId, companyId);
        super.checkPrincipalScopeOrThrow(companyId);

        this.checkHeadquartersUniquenessOrThrow(headquartersId, updatedData.getCity(), updatedData.getAddress(), toUpdate.getCompany());

        toUpdate.setCity(updatedData.getCity());
        toUpdate.setAddress(updatedData.getAddress());
        toUpdate.setPhoneNumber(updatedData.getPhoneNumber());
        toUpdate.setFeDescription(updatedData.getFeDescription());

        super.headquartersRepository.save(toUpdate);
    }

    /**
     * Removing a headquarters.<br>
     *
     * @param companyId      the id of the company of the headquarters to delete
     * @param headquartersId the id of the headquarters to delete
     */
    @Override
    public void remove(Integer companyId, Integer headquartersId) {
        final Headquarters toDelete = super.getHeadquartersOrThrow(headquartersId, companyId);
        super.checkPrincipalScopeOrThrow(companyId);

        this.softDelete(toDelete);
    }

    /**
     * Removing any headquarters for a given company.<br>
     *
     * @param company the company to delete the headquarters of
     */
    @Override
    public void removeAll(Company company) {
        // Get all the headquarters for the company, and soft delete each of them
        super.headquartersRepository.findAllByCompany(company)
                .forEach(this::softDelete);
    }


    /**
     * Internal method to soft delete a headquarters.<br>
     * If any {@link HeadquartersReceptionistUser} still exists for the headquarters, it will not be deleted.<br>
     * Before removing the headquarters, deleting all related workspaces.
     *
     * @param toDelete the resource to be deleted
     */
    private void softDelete(final Headquarters toDelete) {
        // The set of Headquarters Receptionists must be empty
        if (!this.headquartersReceptionistUserService.headquartersReceptionistsFor(toDelete).isEmpty()) {
            throw new HeadquartersNotDeletableException(toDelete.getId());
        }
        this.workspaceService.removeAll(toDelete);
        super.headquartersRepository.softDelete(toDelete);
    }


    /**
     * On creation, checking the headquarters uniqueness amongst company headquarters.
     *
     * @param city    the city of the headquarters
     * @param address the address of the headquarters
     * @param company the company of the headquarters
     */
    private void checkHeadquartersUniquenessOrThrow(final String city, final String address, final Company company) {
        if (this.headquartersRepository.existsByCityAndAddressAndCompany(city, address, company)) {
            throw new HeadquartersAlreadyExistsException(city, address);
        }
    }

    /**
     * On update, checking the headquarters uniqueness amongst company headquarters.
     *
     * @param headquartersId the id of the company to update
     * @param city           the city of the headquarters
     * @param address        the address of the headquarters
     * @param company        the company of the headquarters
     */
    private void checkHeadquartersUniquenessOrThrow(
            final Integer headquartersId,
            final String city,
            final String address,
            final Company company
    ) {
        // The headquarters to update itself should be ignored, when searching for uniqueness constraint !
        if (this.headquartersRepository.existsByIdNotAndCityAndAddressAndCompany(headquartersId, city, address, company)) {
            throw new HeadquartersAlreadyExistsException(city, address);
        }
    }
}
