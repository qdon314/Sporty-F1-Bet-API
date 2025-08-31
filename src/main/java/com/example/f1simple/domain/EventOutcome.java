package com.example.f1simple.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name="event_outcomes")
public class EventOutcome {
  @Id private String eventId;
  @Column(nullable=false) private String winningDriverId;
  @Column(nullable=false) private Instant recordedAt;
  public EventOutcome(){}
  public EventOutcome(String e, String w, Instant r){this.eventId=e;this.winningDriverId=w;this.recordedAt=r;}
  public String getEventId(){return eventId;} public void setEventId(String e){this.eventId=e;}
  public String getWinningDriverId(){return winningDriverId;} public void setWinningDriverId(String w){this.winningDriverId=w;}
  public Instant getRecordedAt(){return recordedAt;} public void setRecordedAt(Instant r){this.recordedAt=r;}
}
