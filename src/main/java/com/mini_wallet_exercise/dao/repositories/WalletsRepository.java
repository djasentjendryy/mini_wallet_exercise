package com.mini_wallet_exercise.dao.repositories;

import com.mini_wallet_exercise.dao.entities.User;
import com.mini_wallet_exercise.dao.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletsRepository extends JpaRepository<Wallet, UUID> {

  Optional<Wallet> findByUser(User user);

  Optional<Wallet> findByUserAndStatus(User user, Wallet.Status status);

  @Transactional
  @Modifying
  @Query("UPDATE Wallet w SET w.balance = w.balance + ?2, w.lastUpdatedAt = ?3 WHERE w.id = ?1")
  int depositBalance(UUID walletId, Long amount, ZonedDateTime now);

  @Transactional
  @Modifying
  @Query("UPDATE Wallet w SET w.balance = w.balance - ?2, w.lastUpdatedAt = ?3 WHERE w.id = ?1")
  int withdrawBalance(UUID walletId, Long amount, ZonedDateTime now);
}
