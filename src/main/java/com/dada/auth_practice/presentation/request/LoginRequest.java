package com.dada.auth_practice.presentation.request;


import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String client_id,
                           @NotBlank String redirect_uri,
                           @NotBlank String response_type) { }
