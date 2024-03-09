package com.mini_wallet_exercise.services;

import com.mini_wallet_exercise.dao.entities.User;
import com.mini_wallet_exercise.dao.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public User createUser(String customerXid) {
    final var customerXidUUID= UUID.fromString(customerXid);
    final var existingUser = userRepository.findByCustomerXid(customerXidUUID);

    if (existingUser.isEmpty()) {
      final var userEntity = User.builder()
        .id(UUID.randomUUID())
        .customerXid(UUID.fromString(customerXid))
        .createdAt(ZonedDateTime.now())
        .build();
      final var user = userRepository.save(userEntity);
      return user;
    }
    return existingUser.get();
  }

  public User verifyUser(String userId) {
    final var user = userRepository.findById(UUID.fromString(userId));
    if (user.isEmpty()) {
      throw new EntityNotFoundException("User not found");
    }
    return user.get();
  }
}
