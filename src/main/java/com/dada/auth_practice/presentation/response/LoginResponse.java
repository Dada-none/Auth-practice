package com.dada.auth_practice.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {
    private String code;
    private String error;
    private String error_description;
    private String state;
}
