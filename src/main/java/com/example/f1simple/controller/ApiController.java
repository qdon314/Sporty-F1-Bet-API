package com.example.f1simple.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.f1simple.domain.Bet;
import com.example.f1simple.dto.BetResponse;
import com.example.f1simple.dto.BetView;
import com.example.f1simple.dto.F1Event;
import com.example.f1simple.dto.OutcomeRequest;
import com.example.f1simple.dto.PlaceBetRequest;
import com.example.f1simple.dto.UserView;
import com.example.f1simple.repo.BetRepository;
import com.example.f1simple.repo.UserRepository;
import com.example.f1simple.service.BettingService;
import com.example.f1simple.service.OpenF1Service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController @RequestMapping @Tag(name="API")
public class ApiController {
  private final OpenF1Service openF1Service; private final BettingService bettingService; private final UserRepository userRepository; private final BetRepository betRepository;
  public ApiController(OpenF1Service o, BettingService b, UserRepository u, BetRepository br){this.openF1Service=o; this.bettingService=b; this.userRepository=u; this.betRepository=br;}

  @Operation(summary="List F1 events")
  @GetMapping("/events")
  public List<F1Event> listEvents(@Parameter(description="e.g., Race, Qualifying") @RequestParam(required=false) String sessionType,
                                  @Parameter(description="e.g., 2023") @RequestParam(required=false) Integer year,
                                  @Parameter(description="OpenF1 country_name") @RequestParam(required=false) String country) {
    return openF1Service.listEvents(sessionType, year, country);
  }

  @Operation(summary="Place a bet")
  @PostMapping("/bets")
  public BetResponse placeBet(@Valid @RequestBody PlaceBetRequest req) {
    Bet bet = bettingService.placeBet(req.userId(), req.eventId(), req.driverId(), req.amount());
    return new BetResponse(bet.getId(), bet.getStatus().name());
  }

  @Operation(summary="Record event outcome")
  @PostMapping("/events/{eventId}/outcome")
  public String recordOutcome(@PathVariable String eventId, @Valid @RequestBody OutcomeRequest req) {
    bettingService.recordOutcome(eventId, req.winningDriverId());
    return "Outcome processed";
  }

  @Operation(summary="Get user by id", description="Returns user's balance.")
  @GetMapping("/users/{id}")
  public UserView getUser(@PathVariable Long id) {
    var user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    return new UserView(user.getId(), user.getBalanceEur());
  }

  @Operation(summary="List bets", description="Optionally filter by userId. Returns most fields for quick inspection.")
  @GetMapping("/bets")
  public java.util.List<BetView> listBets(@RequestParam(required=false) Long userId) {
    java.util.List<Bet> bets = (userId != null) ? betRepository.findByUser_Id(userId) : betRepository.findAll();
    java.util.List<BetView> out = new java.util.ArrayList<>();
    for (Bet b : bets) {
      out.add(new BetView(
          b.getId(),
          b.getUser() != null ? b.getUser().getId() : null,
          b.getEventId(),
          b.getDriverId(),
          b.getOdds(),
          b.getAmount(),
          b.getStatus().name(),
          b.getPlacedAt()
      ));
    }
    return out;
  }

}


