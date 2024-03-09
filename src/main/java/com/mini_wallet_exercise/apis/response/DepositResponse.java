package com.mini_wallet_exercise.apis.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DepositResponse {
  private DepositInfo deposit;

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class DepositInfo {
    private String id;
    @JsonProperty("deposited_by")
    private String depositedBy;
    private String status;
    @JsonProperty("deposited_at)")
    private String depositedAt;
    private Long amount;
    @JsonProperty("reference_id")
    private String referenceId;
  }
}
