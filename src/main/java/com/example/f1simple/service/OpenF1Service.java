package com.example.f1simple.service;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.f1simple.dto.DriverMarket;
import com.example.f1simple.dto.F1Event;

@Service
public class OpenF1Service {
  private final RestTemplate restTemplate = new RestTemplate();
  @Value("${openf1.base-url}") private String baseUrl;

  public List<F1Event> listEvents(String sessionType, Integer year, String country) {
    var sessions = getSessions(sessionType, year, country);
    return sessions.stream().map(s -> new F1Event(
        String.valueOf(s.get("session_key")),
        String.valueOf(s.getOrDefault("session_name","session")),
        String.valueOf(s.getOrDefault("country_name","")),
        s.get("year")==null?null:((Number)s.get("year")).intValue(),
        buildDriverMarket(
          getDrivers(String.valueOf(s.get("session_key")))
        )
    )).collect(Collectors.toList());
  }

  private List<Map<String, Object>> getSessions(String sessionType, Integer year, String country) {
    var b = UriComponentsBuilder.fromHttpUrl(baseUrl + "/sessions");
    if (sessionType != null)
      b.queryParam("session_name", sessionType);
    if (year != null)
      b.queryParam("year", year);
    if (country != null)
      b.queryParam("country_name", country);
    URI uri = b.build(true).toUri();
    try {
      var type = new ParameterizedTypeReference<List<Map<String, Object>>>() {
      };
      var resp = restTemplate.exchange(RequestEntity.get(uri).build(), type);
      return Optional.ofNullable(resp.getBody()).orElse(List.of());
    } catch (Exception e) {
      return List.of();
    }
  }
  
    private List<Map<String,Object>> getDrivers(String sessionKey) {
    var b = UriComponentsBuilder.fromHttpUrl(baseUrl + "/drivers");
    if (sessionKey != null) b.queryParam("session_key", sessionKey);
    URI uri = b.build(true).toUri();
    try {
      var type = new ParameterizedTypeReference<List<Map<String,Object>>>(){};
      var resp = restTemplate.exchange(RequestEntity.get(uri).build(), type);
      return Optional.ofNullable(resp.getBody()).orElse(List.of());
    } catch (Exception e) { return List.of(); }
  }

  private List<DriverMarket> buildDriverMarket(List<Map<String, Object>> drivers) {
    var rnd = ThreadLocalRandom.current();
    var odds = List.of(2,3,4);
    return drivers.stream().limit(20).map(d -> new DriverMarket(
        String.valueOf(d.getOrDefault("driver_number", UUID.randomUUID())),
        (d.getOrDefault("first_name","") + " " + d.getOrDefault("last_name","")).trim(),
        odds.get(rnd.nextInt(odds.size()))
    )).toList();
  }
}
