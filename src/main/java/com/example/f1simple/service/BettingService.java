package com.example.f1simple.service;

import com.example.f1simple.domain.Bet;
import com.example.f1simple.domain.User;
import com.example.f1simple.domain.EventOutcome;
import com.example.f1simple.repo.BetRepository;
import com.example.f1simple.repo.UserRepository;
import com.example.f1simple.repo.EventOutcomeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BettingService {
  private final UserRepository userRepository;
  private final BetRepository betRepository;
  private final EventOutcomeRepository outcomeRepository;
  public BettingService(UserRepository u, BetRepository b, EventOutcomeRepository o) { this.userRepository=u; this.betRepository=b; this.outcomeRepository=o; }

  @Transactional
  public Bet placeBet(Long userId, String eventId, String driverId, BigDecimal amount) {
    User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    if (user.getBalanceEur().compareTo(amount) < 0) throw new IllegalArgumentException("Insufficient balance");
    int odds = List.of(2,3,4).get(ThreadLocalRandom.current().nextInt(3));
    user.setBalanceEur(user.getBalanceEur().subtract(amount));
    userRepository.save(user);
    Bet bet = new Bet();
    bet.setUser(user); bet.setEventId(eventId); bet.setDriverId(driverId); bet.setOdds(odds);
    bet.setAmount(amount); bet.setStatus(Bet.BetStatus.PENDING); bet.setPlacedAt(Instant.now());
    return betRepository.save(bet);
  }

  @Transactional
  public void recordOutcome(String eventId, String winningDriverId) {
    outcomeRepository.save(new EventOutcome(eventId, winningDriverId, Instant.now()));
    var pending = betRepository.findByEventIdAndStatus(eventId, Bet.BetStatus.PENDING);
    for (Bet bet : pending) {
      if (bet.getDriverId().equals(winningDriverId)) {
        bet.setStatus(Bet.BetStatus.WON);
        var prize = bet.getAmount().multiply(BigDecimal.valueOf(bet.getOdds()));
        var user = bet.getUser();
        user.setBalanceEur(user.getBalanceEur().add(prize));
        userRepository.save(user);
      } else {
        bet.setStatus(Bet.BetStatus.LOST);
      }
    }
    betRepository.saveAll(pending);
  }
}
