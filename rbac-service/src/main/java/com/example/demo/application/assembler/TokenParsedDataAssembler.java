package com.example.demo.application.assembler;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.application.shared.dto.TokenValidatedAndParsed;

@Component
public class TokenParsedDataAssembler {

	/**
	 * 組裝 Token Parsed 資料
	 * 
	 * @param username 使用者帳號
	 * @param roles    角色清單
	 * @param groups   群組清單
	 * @param function 功能權限清單
	 * @return 驗證及轉換後的資料
	 */
	public TokenValidatedAndParsed assembleTokenParsed(String username, List<String> roles, List<String> groups,
			List<String> functions) {
		return new TokenValidatedAndParsed(username, roles, groups, functions);
	}
}
