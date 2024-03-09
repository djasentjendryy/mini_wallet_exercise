package com.mini_wallet_exercise.apis.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public abstract class Response<T> {
  private final T data;
  private final String status;
}
