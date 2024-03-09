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
public class EnableWalletResponse {

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

    @JsonProperty("enabled_at")
    private String enabledAt;

    private Long balance;
  }
}
