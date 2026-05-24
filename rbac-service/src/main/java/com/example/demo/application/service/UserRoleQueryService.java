package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.assembler.UserRoleAssembler;
import com.example.demo.application.shared.dto.UserRoleQueried;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.service.UserRoleService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserRoleQueryService {

	private UserRoleAssembler assembler;
	private UserRoleService userRoleService;

	/**
	 * 取得特定使用者所在的角色資料
	 * 
	 * @param username 使用者帳號
	 * @return List<UserRoleQueried>
	 */
	@Transactional(readOnly = true)
	public List<UserRoleQueried> getUserRoles(String username, String service) {
		List<RoleInfo> roles = userRoleService.getUserRoles(username, service);
		log.debug("roles: {}", roles);
		return assembler.transformList(roles);
	}

	/**
	 * 查詢不屬於該使用者的其他角色
	 * 
	 * @param username 使用者名稱
	 * @param service  服務
	 * @return List<UserRoleGroupQueried>
	 */
	@Transactional(readOnly = true)
	public List<UserRoleQueried> getOtherRoles(String username, String service) {
		List<RoleInfo> others = userRoleService.getOtherRoles(username, service);
		return assembler.transformList(others);

	}
}
