package com.example.accountservice.filter;

import com.example.accountservice.model.account.Token;
import com.example.accountservice.service.InvalidTokenService;
import com.example.accountservice.service.TokenService;
import com.example.accountservice.service.UserLoginService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
  private final TokenService tokenService;
  private final InvalidTokenService invalidTokenService;
  @Override
  protected void doFilterInternal(@NonNull final HttpServletRequest httpServletRequest,
                                  @NonNull final HttpServletResponse httpServletResponse,
                                  @NonNull final FilterChain filterChain) throws ServletException, IOException {

    log.debug("API Request was secured with Security!");

    final String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

    if (Token.isBearerToken(authorizationHeader)) {

      final String jwt = Token.getJwt(authorizationHeader);

      tokenService.verifyAndValidate(jwt);

      final String tokenId = tokenService.getId(jwt);

      invalidTokenService.checkForInvalidityOfToken(tokenId);

      final UsernamePasswordAuthenticationToken authentication = tokenService
              .getAuthentication(jwt);

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(httpServletRequest,httpServletResponse);
  }
}
