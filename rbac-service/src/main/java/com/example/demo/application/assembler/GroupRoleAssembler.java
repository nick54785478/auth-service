package com.example.demo.application.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.application.shared.dto.GroupRoleQueried;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.aggregate.vo.RoleProfile;
import com.example.demo.domain.role.aggregate.vo.RoleScope;

@Component
public class GroupRoleAssembler {

	/**
	 * 轉換 Role 資料為群組角色查詢結果
	 * 
	 * @param role 角色資料 (Aggregate Root)
	 * @return {@link GroupRoleQueried}
	 */
	public GroupRoleQueried transform(RoleInfo role) {
		if (role == null) {
			return null;
		}

		// 提取 Value Objects
		RoleScope scope = role.getScope();
		RoleProfile profile = role.getProfile();

		// 防禦性取值，避免資料庫極端髒資料導致 NullPointerException
		String code = (scope != null) ? scope.getCode() : null;
		String name = (profile != null) ? profile.getName() : null;
		String description = (profile != null) ? profile.getDescription() : null;
		return new GroupRoleQueried(role.getId(), code, name, description);
	}

	/**
	 * 轉換 Role 清單資料
	 * 
	 * @param roles 角色資料清單
	 * @return {@link GroupRoleQueried} 清單
	 */
	public List<GroupRoleQueried> transformList(List<RoleInfo> roles) {
		if (roles == null || roles.isEmpty()) {
			return List.of(); // 回傳空集合，避免呼叫端拿到 null
		}
		return roles.stream().map(this::transform).collect(Collectors.toList());
	}
}
