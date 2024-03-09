package com.mini_wallet_exercise.controllers;

import com.mini_wallet_exercise.apis.response.ErrorResponse;
import com.mini_wallet_exercise.apis.response.InitResponse;
import com.mini_wallet_exercise.apis.response.MiniWalletResponse;
import com.mini_wallet_exercise.services.UserService;
import com.mini_wallet_exercise.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.mini_wallet_exercise.constants.FieldNameConstants.FAIL;
import static com.mini_wallet_exercise.constants.FieldNameConstants.REQUEST_ID;
import static com.mini_wallet_exercise.constants.FieldNameConstants.SUCCESS;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@Slf4j
@RestController
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping(value = "/v1/init")
  public ResponseEntity<MiniWalletResponse> createUser(
    @RequestParam("customer_xid") String customerXid,
    final HttpServletRequest httpRequest
  ) {
    final String requestId = (String) httpRequest.getAttribute(REQUEST_ID);
    log.info("RequestId: {}, Create user request received", requestId);

    try {
      final var user = userService.createUser(customerXid);
      final var token = JwtUtil.generateToken(user.getId().toString());

      return ResponseEntity.status(CREATED).body(
        new MiniWalletResponse(
          new InitResponse(token),
          SUCCESS
        )
      );
    } catch (Exception e) {
      log.error("RequestId: {}, Error occurred while creating user, ERROR: {}", requestId, e);
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
        new MiniWalletResponse(
          new ErrorResponse(e.getMessage()),
          FAIL
        )
      );
    }
  }
}
