package com.mini_wallet_exercise.apis.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class InitRequest {
  private final String customerXid;
}
