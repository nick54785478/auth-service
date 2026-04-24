package com.example.demo.infra.context;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.shared.enums.JwtConstants;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 上下文工具類
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContextHolder {

	/**
	 * JWT Claims 的 Key，用於指定 JWT 的 Subject
	 */
	public static final String CLAIM_KEY_SUB = "sub";

	/**
	 * JWT Claims 的 Key，用於指定 JWT 的 Acc
	 */
	public static final String CLAIM_KEY_ACC = "acc";

	/**
	 * Service Header Key
	 */
	public static final String SERVICE_HEADER_KEY = "service-header";

	/**
	 * 儲存從 JWT Token 解析出來的 Claims 內容
	 */
	private static final ThreadLocal<Claims> JWT_CLAIMS = new ThreadLocal<>();

	/**
	 * 儲存目前使用者傳入的 JWT Token
	 */
	private static final ThreadLocal<String> JWT_TOKEN = new ThreadLocal<>();

	/**
	 * 儲存目前使用者傳入的 Service Header
	 */
	private static final ThreadLocal<String> SERVICE_HEADER = new ThreadLocal<>();

	/**
	 * 設置 Service Header
	 * 
	 * @param claims JWT ClaimsSet
	 */
	public static void setService(String service) {
		SERVICE_HEADER.set(service);
	}

	/**
	 * 將 JWT Claims 設定到 ThreadLocal 內。
	 * 
	 * @param claims JWT ClaimsSet
	 */
	public static void setJwtClaims(Claims claims) {
		log.debug("ContextHolder setJwtClaim {}", claims);
		JWT_CLAIMS.set(claims);
	}

	/**
	 * 把 JWT Token 設定到 ThreadLocal 內
	 * 
	 * @param claims
	 */
	public static void setJwtToken(String token) {
		JWT_TOKEN.set(token);
	}

	/**
	 * 取得目前登入者的用戶帳號
	 * 
	 * @return 目前登入者的用戶帳號
	 */
	public static String getUsername() {
		return JWT_CLAIMS.get() != null ? JWT_CLAIMS.get().getSubject() : null;
	}

	/**
	 * 取得服務的請求頭
	 * 
	 * @return 服務的請求頭
	 */
	public static String getService() {
		return SERVICE_HEADER.get() != null ? SERVICE_HEADER.get() : null;
	}

	/**
	 * 取得目前登入者的角色
	 * 
	 * @return 目前登入者的用戶角色
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getRoles() {
		return JWT_CLAIMS.get() != null
				? (List<String>) JWT_CLAIMS.get().get(JwtConstants.JWT_CLAIMS_KEY_ROLE.getValue())
				: new ArrayList<>();
	}

	/**
	 * 取得目前登入者的用戶信箱
	 * 
	 * @return 目前登入者的用戶信箱
	 */
	public static String getUserEmail() {
		return JWT_CLAIMS.get() != null ? (String) JWT_CLAIMS.get().get(JwtConstants.JWT_CLAIMS_KEY_EMAIL.getValue())
				: null;
	}

	/**
	 * 取得目前登入者的 JwToken
	 * 
	 * @return token
	 */
	public static String getJwtoken() {
		return JWT_TOKEN.get() != null ? JWT_TOKEN.get() : null;
	}

	/**
	 * 清除上下文資料
	 */
	public static void clear() {
		JWT_CLAIMS.remove();
		JWT_TOKEN.remove();
		SERVICE_HEADER.remove();
	}

}
