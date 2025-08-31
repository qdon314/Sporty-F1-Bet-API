package com.example.f1simple.domain;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity @Table(name="bets")
public class Bet {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @ManyToOne(optional=false, fetch=FetchType.LAZY) @JoinColumn(name="user_id") private User user;
  @Column(nullable=false) private String eventId;
  @Column(nullable=false) private String driverId;
  @Column(nullable=false) private Integer odds;
  @Column(nullable=false) private BigDecimal amount;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private BetStatus status;
  @Column(nullable=false) private Instant placedAt;
  public enum BetStatus { PENDING, WON, LOST }
  public Bet(){}
  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public User getUser(){return user;} public void setUser(User u){this.user=u;}
  public String getEventId(){return eventId;} public void setEventId(String s){this.eventId=s;}
  public String getDriverId(){return driverId;} public void setDriverId(String s){this.driverId=s;}
  public Integer getOdds(){return odds;} public void setOdds(Integer i){this.odds=i;}
  public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal a){this.amount=a;}
  public BetStatus getStatus(){return status;} public void setStatus(BetStatus s){this.status=s;}
  public Instant getPlacedAt(){return placedAt;} public void setPlacedAt(Instant t){this.placedAt=t;}
}
