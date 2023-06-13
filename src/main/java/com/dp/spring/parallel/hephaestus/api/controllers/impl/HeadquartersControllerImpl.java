package com.dp.spring.parallel.hephaestus.api.controllers.impl;

import com.dp.spring.parallel.hephaestus.api.controllers.HeadquartersController;
import com.dp.spring.parallel.hephaestus.api.dtos.HeadquartersResponseDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.hephaestus.services.WorkplaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RestController
@RequiredArgsConstructor
public class HeadquartersControllerImpl implements HeadquartersController {
    private final HeadquartersService headquartersService;
    private final WorkplaceService workplaceService;


    @Override
    public HeadquartersResponseDTO headquarters(Integer headquartersId) {
        final Headquarters headquarters = this.headquartersService.headquarters(headquartersId);
        final long totalWorkplaces = this.workplaceService.countForHeadquarters(headquarters);
        return HeadquartersResponseDTO.of(totalWorkplaces, headquarters);
    }

    @Override
    public Set<HeadquartersResponseDTO> headquarters() {
        return this.headquartersService.headquarters().stream()
                .map(headquarters -> {
                    long totalWorkplaces = this.workplaceService.countForHeadquarters(headquarters);
                    return HeadquartersResponseDTO.of(totalWorkplaces, headquarters);
                })
                .collect(toSet());
    }

    @Override
    public void toggleFavouriteHeadquarters(Integer headquartersId) {
        this.headquartersService.toggleFavouriteHeadquarters(headquartersId);
    }
}
