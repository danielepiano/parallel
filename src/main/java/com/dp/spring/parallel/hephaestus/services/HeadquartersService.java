package com.dp.spring.parallel.hephaestus.services;

import com.dp.spring.parallel.hephaestus.api.dtos.CompanyHeadquartersResponseDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateHeadquartersRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.HeadquartersResponseDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateHeadquartersRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Company;

import java.util.Set;

public interface HeadquartersService {
    void add(Integer companyId, CreateHeadquartersRequestDTO toAddData);

    HeadquartersResponseDTO headquarters(Integer headquartersId);

    Set<HeadquartersResponseDTO> headquarters();

    Set<CompanyHeadquartersResponseDTO> companyHeadquarters(Integer companyId);

    void update(Integer companyId, Integer headquartersId, UpdateHeadquartersRequestDTO updatedData);

    void remove(Integer companyId, Integer headquartersId);

    void removeAll(Company company);

}
