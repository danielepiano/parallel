package com.dp.spring.parallel.hephaestus.api.controllers.impl;

import com.dp.spring.parallel.hephaestus.api.controllers.HeadquartersController;
import com.dp.spring.parallel.hephaestus.api.dtos.HeadquartersResponseDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RestController
@RequiredArgsConstructor
public class HeadquartersControllerImpl implements HeadquartersController {
    private final HeadquartersService headquartersService;


    @Override
    public HeadquartersResponseDTO headquarters(Integer headquartersId) {
        final Headquarters headquarters = this.headquartersService.headquarters(headquartersId);
        return HeadquartersResponseDTO.of(headquarters);
    }

    @Override
    public Set<HeadquartersResponseDTO> headquarters() {
        return this.headquartersService.headquarters().stream()
                .map(HeadquartersResponseDTO::of)
                .collect(toSet());
    }
}
