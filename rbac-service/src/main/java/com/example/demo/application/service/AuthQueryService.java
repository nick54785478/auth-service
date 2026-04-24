package com.example.demo.application.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.example.demo.domain.service.AuthService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthQueryService {

	private AuthService authService;

	/**
	 * 查詢維護頁面權限清單
	 * 
	 * @param username 使用者名稱
	 * @return List<String> 維護權限清單
	 */
	public List<String> getMaintainPermissions(String username) {
		// 未傳值代表是未登入狀態
		if (StringUtils.isBlank(username)) {
			return new ArrayList<>();
		}
		return authService.getMaintainPermissions(username);
	}

}
