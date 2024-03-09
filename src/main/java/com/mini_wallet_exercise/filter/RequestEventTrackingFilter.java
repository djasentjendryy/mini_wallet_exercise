package com.mini_wallet_exercise.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini_wallet_exercise.apis.response.ErrorResponse;
import com.mini_wallet_exercise.apis.response.MiniWalletResponse;
import com.mini_wallet_exercise.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import static com.mini_wallet_exercise.constants.FieldNameConstants.AUTHORIZATION;
import static com.mini_wallet_exercise.constants.FieldNameConstants.FAIL;
import static com.mini_wallet_exercise.constants.FieldNameConstants.REQUEST_ID;

@Slf4j
@Component
@WebFilter(urlPatterns = "/*")
public class RequestEventTrackingFilter extends OncePerRequestFilter {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(
    final HttpServletRequest httpRequest,
    final HttpServletResponse httpResponse,
    final FilterChain chain
  ) throws ServletException, IOException {

    final var token = httpRequest.getHeader(AUTHORIZATION);
    final var requestId = UUID.randomUUID().toString();
    httpRequest.setAttribute(REQUEST_ID, requestId);

    //expect init request to be unauthenticated
    if (httpRequest.getRequestURI().endsWith("/v1/init")) {
      chain.doFilter(httpRequest, httpResponse);
    } else if (token == null) {
      final var responseBody = new MiniWalletResponse(
        new ErrorResponse("Token not found!"),
        FAIL
      );
      final var responseBodyString = objectMapper.writeValueAsString(responseBody);
      httpResponse.setStatus(403);
      httpResponse.getWriter().write(responseBodyString);
    } else {
      final var jwtToken = token.replace("Token ", "");
      final var expirationDate = JwtUtil.extractExpiration(jwtToken);
      final var isExpired = new Date().after(expirationDate);
      if (isExpired) {
        final var responseBody = new MiniWalletResponse(
          new ErrorResponse("Token expired!"),
          FAIL
        );
        final var responseBodyString = objectMapper.writeValueAsString(responseBody);
        httpResponse.setStatus(403);
        httpResponse.getWriter().write(responseBodyString);
      } else {
        chain.doFilter(httpRequest, httpResponse);
      }
    }
  }

  @Override
  protected boolean shouldNotFilter(final HttpServletRequest request) {
    final var path = request.getRequestURI();
    return path.endsWith("/v1/healthcheck");
  }
}
