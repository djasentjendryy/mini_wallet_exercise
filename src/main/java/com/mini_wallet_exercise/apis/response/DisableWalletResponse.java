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
public class DisableWalletResponse {

  private WalletInfo wallet;

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class WalletInfo {

    private String id;

    @JsonProperty("owned_by")
    private String ownedBy;

    private String status;

    @JsonProperty("disabled_at")
    private String disabledAt;

    private Long balance;
  }
}
