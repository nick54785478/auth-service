package com.example.demo.infra.jwt;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.shared.enums.JwtConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenParser {

	@Value("${jwt.secret.key}")
	private String secretKey;

	@Value("${jwt.token-expiration-seconds}")
	private long tokenExpirationValue; // Token 過期時間(單位: 秒，若是 3600L，即 1 個小時)

	@Value("${jwt.refresh-token-expiration-seconds}")
	private long refreshTokenExpirationValue; // Refresh Token 過期時間 (單位: 秒，如過期時間為 3600L，為 1 小時)

	/**
	 * 從 token 中取得使用者名稱
	 * 
	 * @param token
	 * @return 使用者名稱
	 */
	public String getUsername(String token) {
		log.info("getUsername: {}", this.getTokenBody(token).getSubject());
		log.info("TokenBody: {}", this.getTokenBody(token));
		return getTokenBody(token).getSubject();
	}

	/**
	 * 取得使用者角色
	 * 
	 * @param token
	 * @return 使用者角色
	 */
	@SuppressWarnings(value = "unchecked")
	public List<String> getRoleList(String token) {
		return (List<String>) getTokenBody(token).get(JwtConstants.JWT_CLAIMS_KEY_ROLE.getValue());
	}

	/**
	 * 取得使用者群組
	 * 
	 * @param token
	 * @return 使用者群組
	 */
	@SuppressWarnings(value = "unchecked")
	public List<String> getGroupList(String token) {
		return (List<String>) getTokenBody(token).get(JwtConstants.JWT_CLAIMS_KEY_GROUP.getValue());
	}

	/**
	 * 取得使用者群組
	 * 
	 * @param token
	 * @return 使用者群組
	 */
	@SuppressWarnings(value = "unchecked")
	public List<String> getFuncList(String token) {
		return (List<String>) getTokenBody(token).get(JwtConstants.JWT_CLAIMS_KEY_FUNCTION.getValue());
	}

	/**
	 * 從 token 中取得使用者信箱
	 * 
	 * @param token
	 * @return 使用者信箱
	 */
	public String getEmail(String token) {
		return (String) getTokenBody(token).get(JwtConstants.JWT_CLAIMS_KEY_EMAIL.getValue());
	}

	/**
	 * 取得簽發日
	 * 
	 * @param token
	 * @return 簽發日
	 */
	public Date getIssAt(String token) {
		return getTokenBody(token).getIssuedAt();
	}

	/**
	 * 取得過期日
	 * 
	 * @param token
	 * @return 過期日
	 */
	public Date getExpDate(String token) {
		return getTokenBody(token).getExpiration();
	}

	/**
	 * 是否已過期
	 * 
	 * @param token
	 * @return true/false
	 */
	public boolean isExpiration(String token) {
		return getTokenBody(token).getExpiration().before(new Date());
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
		log.info("Secret Key: {}", secretKey);
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * 解析 Token
	 * 
	 * @param token
	 * @return 解析後的結果 Map
	 */
	public Map<String, Object> parseToken(String token) {
		Claims claims = getTokenBody(token);
		return claims.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

}
