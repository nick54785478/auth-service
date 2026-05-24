package com.example.demo.application.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.application.shared.dto.RoleInfoQueried;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.aggregate.vo.RoleProfile;
import com.example.demo.domain.role.aggregate.vo.RoleScope;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RoleAssembler {

	/**
	 * 轉換 Role 資料
	 * 
	 * @param role 角色資料
	 * @return {@link RoleInfoQueried}
	 */
	public RoleInfoQueried transform(RoleInfo role) {
		RoleScope scope = role.getScope();
		RoleProfile profile = role.getProfile();
		return new RoleInfoQueried(role.getId(), scope.getService(), scope.getCode(), profile.getName(), role.getType(),
				profile.getDescription(), role.getActiveFlag());
	}

	/**
	 * 轉換 Role 清單資料
	 * 
	 * @param role 角色資料清單
	 * @return {@link RoleInfoQueried} 清單
	 */
	public List<RoleInfoQueried> transformList(List<RoleInfo> roles) {
		return roles.stream().map(this::transform).collect(Collectors.toList());

	};
}
