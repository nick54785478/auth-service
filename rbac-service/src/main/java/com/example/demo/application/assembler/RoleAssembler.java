package com.example.demo.application.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.application.shared.dto.RoleInfoQueried;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.aggregate.vo.RoleProfile;
import com.example.demo.domain.role.aggregate.vo.RoleScope;

/**
 * 角色資料轉換器
 */
@Component
public class RoleAssembler {

	/**
	 * 轉換 Role 資料
	 * 
	 * @param role 角色資料
	 * @return {@link RoleInfoQueried}
	 */
	public RoleInfoQueried transform(RoleInfo role) {
		if (role == null) {
			return null;
		}

		RoleScope scope = role.getScope();
		RoleProfile profile = role.getProfile();

		// 使用三元運算子或 Optional 防禦內嵌物件為空的情況 (視你的業務嚴格程度而定)
		String service = scope != null ? scope.getService() : null;
		String code = scope != null ? scope.getCode() : null;
		String name = profile != null ? profile.getName() : null;
		String description = profile != null ? profile.getDescription() : null;

		return new RoleInfoQueried(role.getId(), service, code, name, role.getType(), description,
				role.getActiveFlag());
	}

	/**
	 * 轉換 Role 清單資料
	 * 
	 * @param roles 角色資料清單
	 * @return {@link RoleInfoQueried} 清單
	 */
	public List<RoleInfoQueried> transformList(List<RoleInfo> roles) {
		if (roles == null || roles.isEmpty()) {
			return List.of(); // 回傳空集合，避免回傳 null 造成呼叫端麻煩
		}
		return roles.stream().map(this::transform).collect(Collectors.toList());
	}
}
