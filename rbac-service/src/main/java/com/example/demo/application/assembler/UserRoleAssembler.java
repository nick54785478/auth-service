package com.example.demo.application.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.application.shared.dto.UserRoleQueried;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.aggregate.vo.RoleProfile;
import com.example.demo.domain.role.aggregate.vo.RoleScope;

@Component
public class UserRoleAssembler {

	/**
	 * 將領域實體 (RoleInfo) 轉換為使用者角色查詢結果 DTO
	 * 
	 * @param role 角色資料 (Aggregate Root)
	 * @return {@link UserRoleQueried} 轉換後的 DTO；若傳入 null 則回傳 null
	 */
	public UserRoleQueried transform(RoleInfo role) {
		if (role == null) {
			return null;
		}

		// 1. 提取 Value Objects
		RoleScope scope = role.getScope();
		RoleProfile profile = role.getProfile();

		// 2. 防禦性取值，避免資料庫歷史髒資料導致 NullPointerException
		String service = (scope != null) ? scope.getService() : null;
		String code = (scope != null) ? scope.getCode() : null;
		String name = (profile != null) ? profile.getName() : null;
		String description = (profile != null) ? profile.getDescription() : null;

		// 3. 組裝 DTO (因為 DTO 沒有使用 @Builder，所以採用全參建構子)
		return new UserRoleQueried(role.getId(), service, code, name, role.getType(), description,
				role.getActiveFlag());
	}

	/**
	 * 將領域實體清單 (List<RoleInfo>) 轉換為使用者角色查詢結果清單
	 * 
	 * @param roles 角色資料 (Aggregate Root)清單
	 * @return {@link UserRoleQueried} 清單；若傳入空值或空集合則回傳空 List
	 */
	public List<UserRoleQueried> transformList(List<RoleInfo> roles) {
		if (roles == null || roles.isEmpty()) {
			return List.of(); // 回傳不可變的空集合，保護呼叫端
		}

		return roles.stream().map(this::transform).collect(Collectors.toList());
	}
}