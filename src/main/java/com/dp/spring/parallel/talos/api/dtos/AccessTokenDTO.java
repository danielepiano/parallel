package com.dp.spring.parallel.talos.api.dtos;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Builder
@Value
@RequiredArgsConstructor
public class AccessTokenDTO {
    String token;
}
