package com.dp.spring.parallel.hephaestus.api.controllers.impl;

import com.dp.spring.parallel.hephaestus.api.controllers.HeadquartersController;
import com.dp.spring.parallel.hephaestus.api.dtos.HeadquartersResponseDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.hephaestus.services.WorkplaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

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
    public List<HeadquartersResponseDTO> headquarters() {
        List<Headquarters> favoriteHeadquarters = this.headquartersService.favoriteHeadquarters();
        List<Headquarters> nonFavoriteHeadquarters = this.headquartersService.headquarters();
        nonFavoriteHeadquarters.removeAll(favoriteHeadquarters);

        return Stream.concat(
                favoriteHeadquarters.stream()
                        .map(headquarters -> {
                            long totalWorkplaces = this.workplaceService.countForHeadquarters(headquarters);
                            return HeadquartersResponseDTO.favorite(totalWorkplaces, headquarters);
                        }),
                nonFavoriteHeadquarters.stream()
                        .map(headquarters -> {
                            long totalWorkplaces = this.workplaceService.countForHeadquarters(headquarters);
                            return HeadquartersResponseDTO.nonFavorite(totalWorkplaces, headquarters);
                        })
        ).toList();
    }

    @Override
    public void toggleFavouriteHeadquarters(Integer headquartersId) {
        this.headquartersService.toggleFavouriteHeadquarters(headquartersId);
    }
}
