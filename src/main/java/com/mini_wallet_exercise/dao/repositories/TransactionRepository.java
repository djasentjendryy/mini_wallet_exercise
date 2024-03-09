package com.mini_wallet_exercise.dao.repositories;

import com.mini_wallet_exercise.dao.entities.Transaction;
import com.mini_wallet_exercise.dao.entities.User;
import com.mini_wallet_exercise.dao.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
  List<Transaction> findByUserAndWallet(User user, Wallet wallet);

  Optional<Transaction> findByReferenceId(UUID referenceId);
}
