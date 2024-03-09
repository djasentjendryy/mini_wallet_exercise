package com.mini_wallet_exercise.dao.repositories;

import com.mini_wallet_exercise.dao.entities.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  @Cacheable(value = "user", unless="#result == null")
  Optional<User> findByCustomerXid(UUID customerXid);

  @Override
  @Cacheable(value = "user",  unless="#result == null")
  Optional<User> findById(UUID uuid);
}
