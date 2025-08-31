package com.example.f1simple.dto;

import java.time.Instant;

public record BetView(Long betId, Long userId, String eventId, String driverId, Integer odds,
                      java.math.BigDecimal amount, String status, Instant placedAt) {}
