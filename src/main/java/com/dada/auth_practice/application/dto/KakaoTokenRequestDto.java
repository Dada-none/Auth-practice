package com.dada.auth_practice.application.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoTokenRequestDto {
    private final String grant_type;
    private final String client_id;
    private final String code;
}
