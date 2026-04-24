package com.example.demo.infra.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.application.shared.dto.JwtTokenGenerated;
import com.example.demo.shared.enums.JwtConstants;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {

	@Value("${jwt.secret.key}")
	private String secretKey;

	@Value("${jwt.token-expiration-seconds}")
	private long tokenExpiration; // 過期時間若是3600L秒，即1個小時

	@Value("${jwt.refresh-token-expiration-seconds}")
	private long refreshTokenExpiration; // 過期時間若是3600L秒，即1個小時

	private static final String ISS = "SYSTEM"; // 簽發者

	/**
	 * 建立 JwtToken & RefreshToken
	 * 
	 * @param username  使用者名稱
	 * @param email     使用者信箱
	 * @param roles     - 角色權限清單
	 * @param groups    - 群組清單
	 * @param functions - 功能清單
	 * @return JwtokenGenerated
	 */
	public JwtTokenGenerated generateToken(String username, String email, List<String> roles, List<String> groups,
			List<String> functions) {
		String token = this.generateToken(username, email, roles, groups, functions, tokenExpiration);
		String refreshToken = this.generateRefreshToken(username, email, roles, groups, refreshTokenExpiration);
		return new JwtTokenGenerated(token, refreshToken);
	}

	/**
	 * 建立 token 參數:
	 * 
	 * @param username   使用者名稱
	 * @param email      使用者信箱
	 * @param roles      - 角色權限清單
	 * @param groups     - 群組清單
	 * @param functions  - 功能清單
	 * @param expiration 過期時間 (秒)
	 * @return JWToken
	 */
	public String generateToken(String username, String email, List<String> roles, List<String> groups,
			List<String> functions, long expiration) {
		log.debug("有效時間: {}", expiration);
		HashMap<String, Object> map = new HashMap<>();
		map.put(JwtConstants.JWT_CLAIMS_KEY_EMAIL.getValue(), email);
		map.put(JwtConstants.JWT_CLAIMS_KEY_ROLE.getValue(), roles); // 個人角色
		map.put(JwtConstants.JWT_CLAIMS_KEY_GROUP.getValue(), groups); // 群組角色
		map.put(JwtConstants.JWT_CLAIMS_KEY_FUNCTION.getValue(), functions); // 功能
		log.info("map: {}", map);

		Date issDate = new Date(System.currentTimeMillis()); // 簽發日+7天
		log.info("建立 Token 時的簽發日: {}", issDate);

		Date expDate = new Date(System.currentTimeMillis() + expiration * 1000); // 過期日+7天
		log.info("建立 Token 欲設置的過期日: {}", expDate);
		return Jwts.builder().claims(map) // 角色
				.issuer(ISS) // 簽發者
				.subject(username) // 使用者名稱
				.issuedAt(issDate) // 簽發日期
				.expiration(expDate) // 過期時間
				.signWith(getSigningKey(), Jwts.SIG.HS256) // 密鑰簽名
				.compact();
	}

	/**
	 * 建立 RefreshToken 參數:
	 * 
	 * @param username   使用者名稱
	 * @param email      使用者信箱
	 * @param roles      - 角色權限清單
	 * @param groups     - 群組清單
	 * @param functions  - 功能清單
	 * @param expiration 過期時間 (秒)
	 * @return JWToken
	 */
	public String generateRefreshToken(String username, String email, List<String> roles, List<String> groups,
			long expiration) {
		log.debug("有效時間: {}", expiration);
		HashMap<String, Object> map = new HashMap<>();
		map.put(JwtConstants.JWT_CLAIMS_KEY_EMAIL.getValue(), email);
		map.put(JwtConstants.JWT_CLAIMS_KEY_ROLE.getValue(), roles); // 個人角色
		map.put(JwtConstants.JWT_CLAIMS_KEY_GROUP.getValue(), groups); // 群組角色
		log.info("map: {}", map);

		Date issDate = new Date(System.currentTimeMillis()); // 簽發日+7天
		log.info("建立 Token 時的簽發日: {}", issDate);

		Date expDate = new Date(System.currentTimeMillis() + expiration * 1000); // 過期日+7天
		log.info("建立 Token 欲設置的過期日: {}", expDate);
		return Jwts.builder().claims(map) // 角色
				.issuer(ISS) // 簽發者
				.subject(username) // 使用者名稱
				.issuedAt(issDate) // 簽發日期
				.expiration(expDate) // 過期時間
				.signWith(getSigningKey(), Jwts.SIG.HS256) // 密鑰簽名
				.compact();
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

}
