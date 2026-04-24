package com.example.demo.application.port;

import java.util.List;

import com.example.demo.application.shared.dto.JwtTokenGenerated;

public interface JwTokenManagerPort {

	/**
	 * 建立 token 參數:
	 * 
	 * @param username  使用者名稱
	 * @param roleList  - 角色權限清單
	 * @param groups    - 群組角色清單
	 * @param functions - 功能清單
	 * @return JWToken
	 */
	JwtTokenGenerated generateToken(String username, String email, List<String> roles, List<String> groups,
			List<String> functions);

	/**
	 * 驗證 JWToken 合法性
	 * 
	 * @param token
	 * @return true/false
	 */
	boolean validateToken(String token);

	/**
	 * 取得當前使用者名稱
	 * 
	 * @param token JwToken
	 * @return username
	 */
	String getUsername(String token);

	/**
	 * 取得當前使用者 Email
	 * 
	 * @param token JwToken
	 * @return email
	 */
	String getEmail(String token);

	/**
	 * 取得使用者角色
	 * 
	 * @param token
	 * @return 使用者角色
	 */
	List<String> getRoleList(String token);

	/**
	 * 取得使用者群組
	 * 
	 * @param token
	 * @return 使用者群組
	 */
	List<String> getGroupList(String token);

	/**
	 * 取得使用者功能權限
	 * 
	 * @param token
	 * @return 使用者群組
	 */
	List<String> getFuncList(String token);

	/**
	 * 檢驗 Token 是否過期
	 * 
	 * @param token JwToken
	 * @return 判斷結果
	 */
	boolean isExpiration(String token);

}
