package com.mini_wallet_exercise.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(
  name = "users",
  schema = "mini_wallet"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User implements Serializable {

  @Id
  private UUID id;

  @Column(name = "customer_xid")
  private UUID customerXid;

  @JsonIgnore
  @Column(name = "created_at")
  private ZonedDateTime createdAt;
}
