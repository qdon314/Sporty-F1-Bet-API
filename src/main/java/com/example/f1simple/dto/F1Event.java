package com.example.f1simple.dto;

import java.util.List;

public record F1Event(String sessionKey, String sessionName, String country, Integer year, List<DriverMarket> driverMarket) {}

