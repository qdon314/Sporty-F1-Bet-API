package com.example.f1simple.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name="users")
public class User {
  @Id private Long id;
  @Column(name="balance_eur", nullable=false) private BigDecimal balanceEur;
  public User() {}
  public User(Long id, BigDecimal balanceEur){this.id=id;this.balanceEur=balanceEur;}
  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public BigDecimal getBalanceEur(){return balanceEur;} public void setBalanceEur(BigDecimal b){this.balanceEur=b;}
}
