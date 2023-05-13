package com.dp.spring.parallel.hephaestus.api.controllers.impl;

import com.dp.spring.parallel.hephaestus.api.controllers.CompanyController;
import com.dp.spring.parallel.hephaestus.api.dtos.*;
import com.dp.spring.parallel.hephaestus.services.CompanyService;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class CompanyControllerImpl implements CompanyController {
    private final CompanyService companyService;
    private final HeadquartersService headquartersService;


    @Override
    public void add(CreateCompanyRequestDTO toAddData) {
        this.companyService.add(toAddData);
    }

    @Override
    public CompanyResponseDTO company(Integer companyId) {
        return this.companyService.company(companyId);
    }

    @Override
    public Set<CompanyResponseDTO> companies() {
        return this.companyService.companies();
    }

    @Override
    public void update(Integer companyId, UpdateCompanyRequestDTO updatedData) {
        this.companyService.update(companyId, updatedData);
    }

    @Override
    public void remove(Integer companyId) {
        this.companyService.remove(companyId);
    }


    @Override
    public void addHeadquarters(Integer companyId, CreateHeadquartersRequestDTO toAddData) {
        this.headquartersService.add(companyId, toAddData);
    }

    @Override
    public Set<CompanyHeadquartersResponseDTO> headquarters(Integer companyId) {
        return this.headquartersService.companyHeadquarters(companyId);
    }

    @Override
    public void update(
            final Integer companyId,
            final Integer headquartersId,
            final UpdateHeadquartersRequestDTO updatedData
    ) {
        this.headquartersService.update(companyId, headquartersId, updatedData);
    }

    @Override
    public void removeHeadquarters(Integer companyId, Integer headquartersId) {
        this.headquartersService.remove(companyId, headquartersId);
    }
}
