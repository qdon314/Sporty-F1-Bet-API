package com.example.f1simple.repo;
import com.example.f1simple.domain.Bet;
import com.example.f1simple.domain.Bet.BetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface BetRepository extends JpaRepository<Bet, Long> {
  List<Bet> findByUser_Id(Long userId);
  List<Bet> findByEventIdAndStatus(String eventId, BetStatus status);
}
