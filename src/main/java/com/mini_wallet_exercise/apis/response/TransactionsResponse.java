package com.mini_wallet_exercise.apis.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionsResponse {
  private String id;
  private String referenceId;
  private String transactionType;
  private String status;
  private Long amount;
  private String createdAt;
  private String lastUpdatedAt;
  private String userId;
  private String walletId;
}
