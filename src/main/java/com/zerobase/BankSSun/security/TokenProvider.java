package com.zerobase.BankSSun.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TokenProvider {

  private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1시간

  @Value("${spring.jwt.secret}")
  private String secretKey;

  /**
   * 토큰 생성(발급)_23.07.16
   */
  public String generateToken(String username) {
    Claims claims = Jwts.claims().setSubject(username); // 사용자의 정보를 저장하기 위한 claim

    var now = new Date();
    var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now) // 토큰 생성 시간
        .setExpiration(expiredDate) // 토큰 만료 시간
        .signWith(SignatureAlgorithm.HS512, this.secretKey) // 사용할 암호화 알고리즘, 시크릿 키
        .compact();
  }

  /**
   * 토큰에서 사용자 이름 추출_23.07.16
   */
  public String getUsername(String token) {
    return this.parseClaims(token).getSubject();
  }

  /**
   * 토큰 유효성검사_23.07.16
   */
  public boolean validateToken(String token) {
    if (!StringUtils.hasText(token)) {
      return false;
    }

    var claims = this.parseClaims(token);
    return !claims.getExpiration().before(new Date());
  }

  /**
   * 토큰에서 클레임 정보 추출_23.07.16
   */
  private Claims parseClaims(String token) {
    try {
      return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}
