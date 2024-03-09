package com.mini_wallet_exercise.dao.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
  name = "transactions",
  schema = "mini_wallet"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Transaction implements Serializable {

  @Id
  private UUID id;

  @Column(name="reference_id",nullable = false)
  private UUID referenceId;

  @Enumerated(EnumType.STRING)
  @Column(name="transaction_type",nullable = false)
  private TransactionType transactionType;

  private String status;

  private Long amount;

  @Column(name = "created_at", nullable = false)
  private ZonedDateTime createdAt;

  @Column(name = "last_updated_at", nullable = false)
  private ZonedDateTime lastUpdatedAt;

  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @ManyToOne()
  private User user;

  @JoinColumn(name = "wallet_id", referencedColumnName = "id")
  @ManyToOne()
  private Wallet wallet;

  public enum TransactionType {
    WITHDRAWAL, DEPOSIT
  }
}
