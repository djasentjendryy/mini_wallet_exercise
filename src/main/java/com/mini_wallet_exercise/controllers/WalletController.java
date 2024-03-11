package com.mini_wallet_exercise.controllers;

import com.mini_wallet_exercise.apis.response.DepositResponse;
import com.mini_wallet_exercise.apis.response.DepositResponse.DepositInfo;
import com.mini_wallet_exercise.apis.response.DisableWalletResponse;
import com.mini_wallet_exercise.apis.response.ErrorResponse;
import com.mini_wallet_exercise.apis.response.MiniWalletResponse;
import com.mini_wallet_exercise.apis.response.EnableWalletResponse;
import com.mini_wallet_exercise.apis.response.WithdrawResponse;
import com.mini_wallet_exercise.apis.response.WithdrawResponse.WithdrawInfo;
import com.mini_wallet_exercise.services.TransactionService;
import com.mini_wallet_exercise.services.WalletService;
import com.mini_wallet_exercise.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.ValidationException;
import java.time.format.DateTimeFormatter;

import static com.mini_wallet_exercise.constants.FieldNameConstants.AUTHORIZATION;
import static com.mini_wallet_exercise.constants.FieldNameConstants.FAIL;
import static com.mini_wallet_exercise.constants.FieldNameConstants.REQUEST_ID;
import static com.mini_wallet_exercise.constants.FieldNameConstants.SUCCESS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestController
public class WalletController {

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

  @Autowired
  private WalletService walletService;

  @Autowired
  private TransactionService transactionService;


  @GetMapping(value = "/v1/wallet")
  public ResponseEntity<MiniWalletResponse> getWallet(
    final HttpServletRequest httpRequest
  ) {
    final String requestId = (String) httpRequest.getAttribute(REQUEST_ID);
    log.info("RequestId: {}, Get wallet request received", requestId);
    try {
      final var userId = JwtUtil.extractUserId(httpRequest);
      final var wallet = walletService.getWallet(userId);
      final var walletInfo = EnableWalletResponse.WalletInfo.builder()
        .id(wallet.getId().toString())
        .ownedBy(wallet.getUser().getId().toString())
        .status(wallet.getStatus().toString().toLowerCase())
        .enabledAt(wallet.getEnabledAt().format(DATE_TIME_FORMATTER))
        .balance(wallet.getBalance())
        .build();

      return ResponseEntity.ok(
        new MiniWalletResponse(
          new EnableWalletResponse(walletInfo),
          SUCCESS
        )
      );
    } catch (Exception e) {
      log.error("RequestId: {}, Error occurred while getting wallet, ERROR: {}", requestId, e.getMessage());
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
        new MiniWalletResponse(
          new ErrorResponse(e.getMessage()),
          FAIL
        )
      );
    }
  }

  @PostMapping(value = "/v1/wallet")
  public ResponseEntity<MiniWalletResponse> createWallet(
    final HttpServletRequest httpRequest
  ) {
    final String requestId = (String) httpRequest.getAttribute(REQUEST_ID);
    log.info("RequestId: {}, Create wallet request received", requestId);
    try {
      final var token = httpRequest.getHeader(AUTHORIZATION).replace("Token ", "");
      final var userId = JwtUtil.extractUserId(httpRequest);
      final var wallet = walletService.createWallet(userId);
      final var walletInfo = EnableWalletResponse.WalletInfo.builder()
        .id(wallet.getId().toString())
        .ownedBy(wallet.getUser().getId().toString())
        .status(wallet.getStatus().toString().toLowerCase())
        .enabledAt(wallet.getEnabledAt().format(DATE_TIME_FORMATTER))
        .balance(wallet.getBalance())
        .build();

      return ResponseEntity.status(CREATED).body(
        new MiniWalletResponse(
          new EnableWalletResponse(walletInfo),
          SUCCESS
        )
      );
    } catch (ValidationException e) {
      log.error("RequestId: {}, Error occurred while depositing to wallet, ERROR: {}", requestId, e.getMessage());
      return ResponseEntity.status(BAD_REQUEST).body(
        new MiniWalletResponse(
          new ErrorResponse(e.getMessage()),
          FAIL
        )
      );
    } catch (Exception e) {
      log.error("RequestId: {}, Error occurred while creating wallet, ERROR: {}", requestId, e.getMessage());
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
        new MiniWalletResponse(
          new ErrorResponse(e.getMessage()),
          FAIL
        )
      );
    }
  }

  @PatchMapping(value = "/v1/wallet")
  public ResponseEntity<MiniWalletResponse> disableWallet(
    final HttpServletRequest httpRequest
  ) {
    final String requestId = (String) httpRequest.getAttribute(REQUEST_ID);
    log.info("RequestId: {}, Disable wallet request received", requestId);
    try {
      final var token = httpRequest.getHeader(AUTHORIZATION).replace("Token ", "");
      final var userId = JwtUtil.extractUserId(httpRequest);
      final var wallet = walletService.disableWallet(userId);
      final var walletInfo = DisableWalletResponse.WalletInfo.builder()
        .id(wallet.getId().toString())
        .ownedBy(wallet.getUser().getId().toString())
        .status(wallet.getStatus().toString().toLowerCase())
        .disabledAt(wallet.getLastUpdatedAt().format(DATE_TIME_FORMATTER))
        .balance(wallet.getBalance())
        .build();

      return ResponseEntity.status(CREATED).body(
        new MiniWalletResponse(
          new DisableWalletResponse(walletInfo),
          SUCCESS
        )
      );
    } catch (Exception e) {
      log.error("RequestId: {}, Error occurred while disabling wallet, ERROR: {}", requestId, e.getMessage());
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
        new MiniWalletResponse(
          new ErrorResponse(e.getMessage()),
          FAIL
        )
      );
    }
  }

  @GetMapping(value = "/v1/wallet/transactions")
  public ResponseEntity<MiniWalletResponse> getWalletTransactions(
    final HttpServletRequest httpRequest
  ) {
    final String requestId = (String) httpRequest.getAttribute(REQUEST_ID);
    log.info("RequestId: {}, Get wallet transactions request received", requestId);
    try {
      final var userId = JwtUtil.extractUserId(httpRequest);
      final var transactions = transactionService.getTransactions(userId);
      return ResponseEntity.ok(
        new MiniWalletResponse(
          transactions,
          SUCCESS
        )
      );
    } catch (Exception e) {
      log.error("RequestId: {}, Error occurred while getting wallet transactions, ERROR: {}", requestId, e.getMessage());
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
        new MiniWalletResponse(
          new ErrorResponse(e.getMessage()),
          FAIL
        )
      );
    }
  }

  @PostMapping(value = "/v1/wallet/deposits")
  public ResponseEntity<MiniWalletResponse> depositToWallet(
    @RequestParam("amount") String amount,
    @RequestParam("reference_id") String referenceId,
    final HttpServletRequest httpRequest
  ) {
    final String requestId = (String) httpRequest.getAttribute(REQUEST_ID);
    log.info("RequestId: {}, Deposit to wallet request received", requestId);
    try {
      final var token = httpRequest.getHeader(AUTHORIZATION).replace("Token ", "");
      final var userId = JwtUtil.extractUserId(httpRequest);
      final var transaction = transactionService.deposit(userId, Long.parseLong(amount), referenceId);
      final var depositInfo = DepositInfo.builder()
        .id(transaction.getId().toString())
        .referenceId(transaction.getReferenceId().toString())
        .status(transaction.getStatus().toLowerCase())
        .amount(transaction.getAmount())
        .depositedBy(transaction.getUser().getId().toString())
        .depositedAt(transaction.getCreatedAt().format(DATE_TIME_FORMATTER))
        .build();

      return ResponseEntity.ok(
        new MiniWalletResponse(
          new DepositResponse(depositInfo),
          SUCCESS
        )
      );
    } catch (ValidationException e) {
      log.error("RequestId: {}, Error occurred while depositing to wallet, ERROR: {}", requestId, e.getMessage());
      return ResponseEntity.status(BAD_REQUEST).body(
        new MiniWalletResponse(
          new ErrorResponse(e.getMessage()),
          FAIL
        )
      );
    } catch (Exception e) {
      log.error("RequestId: {}, Error occurred while depositing to wallet, ERROR: {}", requestId, e.getMessage());
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
        new MiniWalletResponse(
          new ErrorResponse(e.getMessage()),
          FAIL
        )
      );
    }
  }

  @PostMapping(value = "/v1/wallet/withdrawals")
  public ResponseEntity<MiniWalletResponse> withdrawToWallet(
    @RequestParam("amount") String amount,
    @RequestParam("reference_id") String referenceId,
    final HttpServletRequest httpRequest
  ) {
    final String requestId = (String) httpRequest.getAttribute(REQUEST_ID);
    log.info("RequestId: {}, Deposit to wallet request received", requestId);
    try {
      final var userId = JwtUtil.extractUserId(httpRequest);
      final var transaction = transactionService.withdraw(userId, Long.parseLong(amount), referenceId);
      final var depositInfo = WithdrawInfo.builder()
        .id(transaction.getId().toString())
        .referenceId(transaction.getReferenceId().toString())
        .status(transaction.getStatus().toLowerCase())
        .amount(transaction.getAmount())
        .withdrawnBy(transaction.getUser().getId().toString())
        .withdrawnAt(transaction.getCreatedAt().format(DATE_TIME_FORMATTER))
        .build();

      return ResponseEntity.ok(
        new MiniWalletResponse(
          new WithdrawResponse(depositInfo),
          SUCCESS
        )
      );
    } catch (ValidationException e) {
      log.error("RequestId: {}, Error occurred while depositing to wallet, ERROR: {}", requestId, e.getMessage());
      return ResponseEntity.status(BAD_REQUEST).body(
        new MiniWalletResponse(
          new ErrorResponse(e.getMessage()),
          FAIL
        )
      );
    } catch (Exception e) {
      log.error("RequestId: {}, Error occurred while depositing to wallet, ERROR: {}", requestId, e.getMessage());
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
        new MiniWalletResponse(
          new ErrorResponse(e.getMessage()),
          FAIL
        )
      );
    }
  }
}
