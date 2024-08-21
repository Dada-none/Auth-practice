package com.dada.auth_practice.presentation.request;


import jakarta.validation.constraints.NotBlank;

public record TokenRequest(@NotBlank String grant_type,
                           @NotBlank String client_id,
                           @NotBlank String redirect_uri,
                           @NotBlank String code
                           ) { }
