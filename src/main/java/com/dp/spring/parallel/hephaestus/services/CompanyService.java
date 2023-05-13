package com.dp.spring.parallel.hephaestus.services;

import com.dp.spring.parallel.hephaestus.api.dtos.CompanyResponseDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateCompanyRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateCompanyRequestDTO;

import java.util.Set;

public interface CompanyService {
    void add(CreateCompanyRequestDTO toAddData);

    CompanyResponseDTO company(Integer companyId);

    Set<CompanyResponseDTO> companies();

    void update(Integer companyId, UpdateCompanyRequestDTO updatedData);

    void remove(Integer companyId);
}
