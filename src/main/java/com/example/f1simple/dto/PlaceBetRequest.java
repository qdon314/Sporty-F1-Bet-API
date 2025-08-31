package com.example.f1simple.dto;
import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlaceBetRequest(@NotNull Long userId, @NotBlank String eventId, @NotBlank String driverId, @NotNull @DecimalMin("1.0") BigDecimal amount) {}