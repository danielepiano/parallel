package com.dp.spring.parallel.hephaestus.services.impl;

import com.dp.spring.parallel.agora.services.EventService;
import com.dp.spring.parallel.common.exceptions.HeadquartersAlreadyExistsException;
import com.dp.spring.parallel.common.exceptions.HeadquartersNotDeletableException;
import com.dp.spring.parallel.common.exceptions.HeadquartersNotFoundException;
import com.dp.spring.parallel.common.services.BusinessService;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateHeadquartersRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateHeadquartersRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.hephaestus.services.WorkspaceService;
import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.enums.UserRole;
import com.dp.spring.parallel.hestia.services.HeadquartersReceptionistUserService;
import com.dp.spring.springcore.observer.ObserverService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Headquarters operations service implementation.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HeadquartersServiceImpl extends BusinessService implements HeadquartersService {
    private final HeadquartersReceptionistUserService headquartersReceptionistUserService;
    private final WorkspaceService workspaceService;
    private final EventService eventService;


    /**
     * {@inheritDoc}
     *
     * @param companyId the id of the company of the headquarters to add
     * @param toAddData the data of the headquarters to add
     * @return the created headquarters
     */
    @Override
    public Headquarters add(Integer companyId, CreateHeadquartersRequestDTO toAddData) {
        final Company company = super.getCompanyOrThrow(companyId);
        super.checkPrincipalScopeOrThrow(companyId);

        this.checkHeadquartersUniquenessOrThrow(toAddData.getCity(), toAddData.getAddress(), company);

        final Headquarters toAdd = new Headquarters()
                .setCity(toAddData.getCity())
                .setAddress(toAddData.getAddress())
                .setPhoneNumber(toAddData.getPhoneNumber())
                .setDescription(toAddData.getDescription())
                .setCompany(company);

        return super.headquartersRepository.save(toAdd);
    }

    /**
     * {@inheritDoc}
     *
     * @param headquartersId the id of the headquarters to retrieve
     * @return the headquarters
     */
    @Override
    public Headquarters headquarters(Integer headquartersId) {
        return super.getResourceOrThrow(
                headquartersId,
                super.headquartersRepository,
                new HeadquartersNotFoundException(headquartersId)
        );
    }

    /**
     * {@inheritDoc}
     *
     * @return the headquarters
     */
    @Override
    public List<Headquarters> headquarters() {
        final User principal = super.getPrincipalOrThrow();

        // If principal cannot have favorite headquarters, return all headquarters
        if (!UserRole.COMPANY_MANAGER.equals(principal.getRole()) && !UserRole.EMPLOYEE.equals(principal.getRole())) {
            return this.headquartersRepository.findAll();
        }
        // If principal can set favorite headquarters, firstly return the favorite ones, then the others
        List<Headquarters> nonFavHeadquarters = this.headquartersRepository.findAll();
        nonFavHeadquarters.removeAll(principal.getFavoriteHeadquarters());

        return Stream.concat(principal.getFavoriteHeadquarters().stream(), nonFavHeadquarters.stream()).collect(toList());
    }

    /**
     * {@inheritDoc}
     *
     * @return the favorite headquarters
     */
    @Override
    public List<Headquarters> favoriteHeadquarters() {
        return super.getPrincipalOrThrow().getFavoriteHeadquarters();
    }

    /**
     * {@inheritDoc}
     *
     * @param companyId the id of the company
     * @return the headquarters of the company
     */
    @Override
    public Set<Headquarters> companyHeadquarters(Integer companyId) {
        return super.headquartersRepository.findAllByCompany(super.getCompanyOrThrow(companyId));
    }

    /**
     * {@inheritDoc}
     *
     * @param companyId      the id of the company of the headquarters to update
     * @param headquartersId the id of the headquarters to update
     * @param updatedData    the new data of the headquarters to update
     * @return the updated headquarters
     */
    @Override
    public Headquarters update(Integer companyId, Integer headquartersId, UpdateHeadquartersRequestDTO updatedData) {
        final Headquarters toUpdate = super.getHeadquartersOrThrow(headquartersId, companyId);
        super.checkPrincipalScopeOrThrow(companyId);

        this.checkHeadquartersUniquenessOrThrow(headquartersId, updatedData.getCity(), updatedData.getAddress(), toUpdate.getCompany());

        toUpdate.setCity(updatedData.getCity());
        toUpdate.setAddress(updatedData.getAddress());
        toUpdate.setPhoneNumber(updatedData.getPhoneNumber());
        toUpdate.setDescription(updatedData.getDescription());

        return super.headquartersRepository.save(toUpdate);
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
     *
     * @param headquartersId the id of the headquarters to subscribe/unsubscribe the principal to/from
     */
    @Override
    public void toggleFavouriteHeadquarters(Integer headquartersId) {
        final Headquarters headquarters = super.getHeadquartersOrThrow(headquartersId);
        final User worker = super.getPrincipalOrThrow();

        if (headquarters.getObservers().stream().anyMatch(worker::equals)) {
            this.removeObserver(worker, headquarters);
        } else {
            this.addObserver(worker, headquarters);
        }
    }

    /**
     * Subscribing a given observer (worker) to the given publisher (headquarters).
     *
     * @param worker       the observer to subscribe
     * @param headquarters the publisher to subscribe the observer to
     */
    @Override
    public void addObserver(User worker, Headquarters headquarters) {
        headquarters.getObservers().add(worker);
        this.headquartersRepository.save(headquarters);
    }

    /**
     * Unsubscribing a given observer (worker) from the given publisher (headquarters).
     *
     * @param worker       the observer to unsubscribe
     * @param headquarters the publisher to unsubscribe the observer from
     */
    @Override
    public void removeObserver(User worker, Headquarters headquarters) {
        headquarters.getObservers().remove(worker);
        this.headquartersRepository.save(headquarters);
    }

    /**
     * Notifying each observer to react to a headquarters event, according to the given reaction strategy.
     *
     * @param headquarters    the publisher triggering the event
     * @param observerService the strategy defining the reaction for each observer to call
     * @param context         the context to consider for observer to react properly
     * @param <C>             the type of the context to consider for observer to react properly
     * @param <OS>            an observer service strategy to call for triggering the reaction of each observer
     */
    @Override
    public <C, OS extends ObserverService<User, Headquarters, C>>
    void notifyObservers(Headquarters headquarters, OS observerService, C context) {
        log.info("Notifying observers of headquarters {}...", headquarters.getId());

        headquarters.getObservers().forEach(
                observer -> observerService.react(observer, headquarters, context)
        );
    }

    /**
     * Internal method to soft delete a headquarters.<br>
     * If any {@link HeadquartersReceptionistUser} still exists for the headquarters, it will not be deleted.<br>
     * Before removing the headquarters, deleting all related workspaces and events.
     *
     * @param toDelete the resource to be deleted
     */
    private void softDelete(final Headquarters toDelete) {
        // The set of Headquarters Receptionists must be empty
        if (!this.headquartersReceptionistUserService.headquartersReceptionistsFor(toDelete).isEmpty()) {
            throw new HeadquartersNotDeletableException(toDelete.getId());
        }
        this.workspaceService.removeAll(toDelete);
        this.eventService.removeAll(toDelete);
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
