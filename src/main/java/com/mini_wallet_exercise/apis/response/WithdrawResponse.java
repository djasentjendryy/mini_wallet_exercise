package com.mini_wallet_exercise.apis.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WithdrawResponse {
  private WithdrawInfo withdrawal;

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class WithdrawInfo {
    private String id;
    @JsonProperty("withdrawn_by")
    private String withdrawnBy;
    private String status;
    @JsonProperty("withdrawn_at")
    private String withdrawnAt;
    private Long amount;
    @JsonProperty("reference_id")
    private String referenceId;
  }
}
