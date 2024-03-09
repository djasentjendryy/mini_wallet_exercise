package com.mini_wallet_exercise.utils;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Base64;
import java.util.Date;

import static com.mini_wallet_exercise.constants.FieldNameConstants.AUTHORIZATION;

public class JwtUtil {
  private static final String SECRET = "customer-token";
  private static final long EXPIRATION_TIME = 3_600_000; // 1 hour

  public static String generateToken(String customerXid) {
    String encodedSecret = Base64.getEncoder().encodeToString(SECRET.getBytes());

    return Jwts.builder()
      .setSubject(customerXid)
      .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
      .signWith(
        SignatureAlgorithm.HS256,
        encodedSecret
      )
      .compact();
  }

  public static String extractUserId(final HttpServletRequest httpRequest) {
    final var token = httpRequest.getHeader(AUTHORIZATION).replace("Token ", "");
    String encodedSecret = Base64.getEncoder().encodeToString(SECRET.getBytes());
    return Jwts.parser()
      .setSigningKey(
        encodedSecret
      )
      .parseClaimsJws(token)
      .getBody()
      .getSubject();
  }


  public static Date extractExpiration(String token) {
    String encodedSecret = Base64.getEncoder().encodeToString(SECRET.getBytes());
    return Jwts.parser()
      .setSigningKey(
        encodedSecret
      )
      .parseClaimsJws(token)
      .getBody()
      .getExpiration();
  }
}
