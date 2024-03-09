package com.mini_wallet_exercise.dao.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(
  name = "wallets",
  schema = "mini_wallet"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Wallet implements Serializable {

  @Id
  private UUID id;

  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @OneToOne()
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status;

  @Column(name = "balance", nullable = false)
  private Long balance;

  @Column(name = "enabled_at", nullable = false)
  private ZonedDateTime enabledAt;

  @Column(name = "created_at", nullable = false)
  private ZonedDateTime createdAt;

  @Column(name = "last_updated_at", nullable = false)
  private ZonedDateTime lastUpdatedAt;

  public enum Status {
    ENABLED, DISABLED
  }
}

