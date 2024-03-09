package com.mini_wallet_exercise.services;

import com.mini_wallet_exercise.apis.response.TransactionsResponse;
import com.mini_wallet_exercise.dao.entities.Transaction;
import com.mini_wallet_exercise.dao.entities.Transaction.TransactionType;
import com.mini_wallet_exercise.dao.repositories.TransactionRepository;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.ValidationException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mini_wallet_exercise.constants.FieldNameConstants.SUCCESS;

@Service
public class TransactionService {

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private WalletService walletService;

  @Autowired
  private UserService userService;

  @Transactional
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  public Transaction deposit(
    final String userId,
    final Long amount,
    final String referenceId
  ) throws ValidationException {
    final var now = ZonedDateTime.now();
    final var user = userService.verifyUser(userId);
    final var wallet = walletService.getActiveWallet(user.getId().toString());

    if (amount <= 0) {
      throw new ValidationException("Amount should be greater than 0");
    }

    walletService.depositBalance(wallet.getId(), amount);

    final var transaction = Transaction.builder()
      .id(UUID.randomUUID())
      .referenceId(UUID.fromString(referenceId))
      .transactionType(Transaction.TransactionType.DEPOSIT)
      .status(SUCCESS)
      .amount(amount)
      .user(user)
      .wallet(wallet)
      .createdAt(now)
      .lastUpdatedAt(now)
      .build();
    return transactionRepository.save(transaction);
  }

  @Transactional
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  public Transaction withdraw(
    final String userId,
    final Long amount,
    final String referenceId
  ) throws ValidationException {
    final var now = ZonedDateTime.now();
    final var user = userService.verifyUser(userId);
    final var wallet = walletService.getActiveWallet(user.getId().toString());

    if (amount > wallet.getBalance()) {
      throw new ValidationException("Insufficient balance");
    }

    walletService.withdrawBalance(wallet.getId(), amount);

    final var transaction = Transaction.builder()
      .id(UUID.randomUUID())
      .referenceId(UUID.fromString(referenceId))
      .transactionType(TransactionType.WITHDRAWAL)
      .status(SUCCESS)
      .amount(amount)
      .user(user)
      .wallet(wallet)
      .createdAt(now)
      .lastUpdatedAt(now)
      .build();

    return transactionRepository.save(transaction);
  }

  public List<TransactionsResponse> getTransactions(final String userId) {
    final var user = userService.verifyUser(userId);
    final var wallet = walletService.getActiveWallet(user.getId().toString());
    final var trasactions =  transactionRepository.findByUserAndWallet(user, wallet);
    return convertToTransactionsResponse(trasactions);
  }

  private List<TransactionsResponse> convertToTransactionsResponse(
    final List<Transaction> transactions
  ) {
    final List<TransactionsResponse> transactionResponses = new ArrayList<>();
    transactions.forEach(
      transaction -> {
        final var transactionResponse = TransactionsResponse.builder()
          .id(transaction.getId().toString())
          .referenceId(transaction.getReferenceId().toString())
          .transactionType(transaction.getTransactionType().toString())
          .status(transaction.getStatus())
          .amount(transaction.getAmount())
          .createdAt(transaction.getCreatedAt().format(DATE_TIME_FORMATTER))
          .lastUpdatedAt(transaction.getLastUpdatedAt().format(DATE_TIME_FORMATTER))
          .userId(transaction.getUser().getId().toString())
          .walletId(transaction.getWallet().getId().toString())
          .build();
        transactionResponses.add(transactionResponse);
      }
    );

    return transactionResponses;
  }
}
