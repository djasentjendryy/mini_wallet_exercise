package com.mini_wallet_exercise.apis.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class TransactionRequest {
  private String amount;
  @JsonProperty("reference_id")
  private String referenceId;
}
