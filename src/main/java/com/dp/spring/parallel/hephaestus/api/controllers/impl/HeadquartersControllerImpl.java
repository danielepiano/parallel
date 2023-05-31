package com.dp.spring.parallel.hephaestus.api.controllers.impl;

import com.dp.spring.parallel.hephaestus.api.controllers.HeadquartersController;
import com.dp.spring.parallel.hephaestus.api.dtos.HeadquartersResponseDTO;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class HeadquartersControllerImpl implements HeadquartersController {
    private final HeadquartersService headquartersService;


    @Override
    public HeadquartersResponseDTO headquarters(Integer headquartersId) {
        return this.headquartersService.headquarters(headquartersId);
    }

    @Override
    public Set<HeadquartersResponseDTO> headquarters() {
        return this.headquartersService.headquarters();
    }
}
