package com.mini_wallet_exercise.services;

import com.mini_wallet_exercise.dao.entities.User;
import com.mini_wallet_exercise.dao.entities.Wallet;
import com.mini_wallet_exercise.dao.repositories.WalletsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.time.ZonedDateTime;
import java.util.UUID;

import static com.mini_wallet_exercise.dao.entities.Wallet.Status.ENABLED;

@Service
public class WalletService {

  @Autowired
  private WalletsRepository walletsRepository;

  @Autowired
  private UserService userService;

  public Wallet getWallet(final String userId) {
    final var user = userService.verifyUser(userId);
    return walletsRepository.findByUser(user)
      .orElseThrow(() -> new EntityNotFoundException("Wallet not found"));
  }

  public Wallet getActiveWallet(final String userId) {
    final var user = userService.verifyUser(userId);
    return walletsRepository.findByUserAndStatus(user, ENABLED)
      .orElseThrow(() -> new EntityNotFoundException("Wallet disabled or not found"));
  }

  public Wallet createWallet(final String userId) throws ValidationException {
    final var now = ZonedDateTime.now();
    final var user = userService.verifyUser(userId);

    final var existingWalletOptional = walletsRepository.findByUser(user);
    if (existingWalletOptional.isEmpty()) {
      return createNewWallet(user, now);
    } else {
      final var existingWallet = existingWalletOptional.get();
      if (existingWallet.getStatus().equals(ENABLED)) {
        throw new ValidationException("Wallet already exists and enabled");
      } else {
        return (enableWallet(existingWallet, now));
      }
    }
  }

  public Wallet disableWallet(final String userId) {
    final var user = userService.verifyUser(userId);
    final var wallet = getActiveWallet(user.getId().toString());
    wallet.setStatus(Wallet.Status.DISABLED);
    wallet.setLastUpdatedAt(ZonedDateTime.now());
    return walletsRepository.save(wallet);
  }

  public void depositBalance(final UUID walletId, final Long amount) {
    final var now = ZonedDateTime.now();
    walletsRepository.depositBalance(walletId, amount, now);
  }

  public void withdrawBalance(final UUID walletId, final Long amount) {
    final var now = ZonedDateTime.now();
    walletsRepository.withdrawBalance(walletId, amount, now);
  }

  private Wallet createNewWallet(final User user, final ZonedDateTime now) {
    final var wallet = Wallet.builder()
      .id(UUID.randomUUID())
      .user(user)
      .balance(0L)
      .status(ENABLED)
      .enabledAt(now)
      .createdAt(now)
      .lastUpdatedAt(now)
      .build();
    return walletsRepository.save(wallet);
  }

  private Wallet enableWallet(final Wallet wallet, final ZonedDateTime now) {
    wallet.setStatus(ENABLED);
    wallet.setEnabledAt(now);
    wallet.setLastUpdatedAt(now);
    return walletsRepository.save(wallet);
  }
}
