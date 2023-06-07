package com.dp.spring.parallel.hephaestus.api.controllers.impl;

import com.dp.spring.parallel.hephaestus.api.controllers.CompanyController;
import com.dp.spring.parallel.hephaestus.api.dtos.*;
import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.services.CompanyService;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RestController
@RequiredArgsConstructor
public class CompanyControllerImpl implements CompanyController {
    private final CompanyService companyService;
    private final HeadquartersService headquartersService;


    @Override
    public CompanyResponseDTO add(CreateCompanyRequestDTO toAddData) {
        final Company company = this.companyService.add(toAddData);
        return CompanyResponseDTO.of(company);
    }

    @Override
    public CompanyResponseDTO company(Integer companyId) {
        final Company company = this.companyService.company(companyId);
        return CompanyResponseDTO.of(company);
    }

    @Override
    public Set<CompanyResponseDTO> companies() {
        return this.companyService.companies().stream()
                .map(CompanyResponseDTO::of)
                .collect(toSet());
    }

    @Override
    public CompanyResponseDTO update(Integer companyId, UpdateCompanyRequestDTO updatedData) {
        final Company company = this.companyService.update(companyId, updatedData);
        return CompanyResponseDTO.of(company);
    }

    @Override
    public void remove(Integer companyId) {
        this.companyService.remove(companyId);
    }


    @Override
    public CompanyHeadquartersResponseDTO addHeadquarters(Integer companyId, CreateHeadquartersRequestDTO toAddData) {
        final Headquarters headquarters = this.headquartersService.add(companyId, toAddData);
        return CompanyHeadquartersResponseDTO.of(headquarters);
    }

    @Override
    public Set<CompanyHeadquartersResponseDTO> headquarters(Integer companyId) {
        return this.headquartersService.companyHeadquarters(companyId).stream()
                .map(CompanyHeadquartersResponseDTO::of)
                .collect(toSet());
    }

    @Override
    public CompanyHeadquartersResponseDTO update(
            final Integer companyId,
            final Integer headquartersId,
            final UpdateHeadquartersRequestDTO updatedData
    ) {
        final Headquarters headquarters = this.headquartersService.update(companyId, headquartersId, updatedData);
        return CompanyHeadquartersResponseDTO.of(headquarters);
    }

    @Override
    public void removeHeadquarters(Integer companyId, Integer headquartersId) {
        this.headquartersService.remove(companyId, headquartersId);
    }
}
