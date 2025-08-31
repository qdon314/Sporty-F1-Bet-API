package com.example.f1simple.dto;

import jakarta.validation.constraints.NotBlank;

public record OutcomeRequest(@NotBlank String winningDriverId) {}

