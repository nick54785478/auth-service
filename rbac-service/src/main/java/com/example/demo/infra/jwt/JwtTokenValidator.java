package com.example.demo.infra.jwt;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

	@Value("${jwt.secret.key}")
	private String scretKey;

	@Value("${jwt.token-expiration-seconds}")
	private long expiration; // 過期時間若是3600L秒，即1個小時

	/**
	 * 驗證 JWToken 合法性
	 * 
	 * @param token
	 * @return true/false
	 */
	public boolean validateToken(String token) {
		Claims claims = this.getTokenBody(token);
		return claims != null;
	}

	/**
	 * 取得 Token 主體
	 * 
	 * @param token
	 * @return Claims
	 */
	public Claims getTokenBody(String token) {
		// 使用 Jwts.parser() 建立 JwtParser 實例
		return Jwts.parser().verifyWith(getSigningKey()) // 用於設定金鑰，該金鑰用於驗證 JWT 令牌的簽名
				.build() // 建置 JwtParser 實例
				.parseSignedClaims(token).getPayload();
	}

	/**
	 * 取得 SigningKey
	 * 
	 * @return key
	 */
	private SecretKey getSigningKey() {
		log.info("Secret Key: {}", scretKey);
		byte[] keyBytes = Decoders.BASE64.decode(scretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
