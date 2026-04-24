package com.example.demo.application.service;

import org.springframework.stereotype.Service;

import com.example.demo.application.assembler.TokenParsedDataAssembler;
import com.example.demo.application.port.JwTokenManagerPort;
import com.example.demo.application.shared.dto.TokenValidatedAndParsed;
import com.example.demo.infra.exception.ValidationException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthCommandService {

	private JwTokenManagerPort jwTokenManager;
	private TokenParsedDataAssembler assembler;

	/**
	 * 驗證及轉換 Token 資料
	 * 
	 * @param token  JWToken
	 * @param 轉換後的資料
	 * 
	 */
	public TokenValidatedAndParsed validateAndParseToken(String token) {
		boolean validated = jwTokenManager.validateToken(token);
		if (!validated) {
			throw new ValidationException("VALIDATE_FAILED", "Token 驗證失敗，權限有問題");
		}
		return assembler.assembleTokenParsed(jwTokenManager.getUsername(token), jwTokenManager.getRoleList(token),
				jwTokenManager.getGroupList(token), jwTokenManager.getFuncList(token));
	}
}
