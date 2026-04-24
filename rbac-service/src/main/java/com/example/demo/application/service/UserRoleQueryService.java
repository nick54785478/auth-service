package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.shared.dto.UserRoleQueried;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.service.UserRoleService;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserRoleQueryService {

	private UserRoleService userRoleService;

	/**
	 * 取得特定使用者所在的 Role 資料
	 * 
	 * @param username 使用者帳號
	 * @return List<UserRoleQueried>
	 */
	@Transactional
	public List<UserRoleQueried> queryRoles(String username, String service) {
		List<RoleInfo> roles = userRoleService.queryRoles(username, service);
		log.debug("roles: {}", roles);
		return BaseDataTransformer.transformData(roles, UserRoleQueried.class);
	}

	/**
	 * 查詢不屬於該使用者的其他角色
	 * 
	 * @param username
	 * @param service  服務
	 * @return List<UserRoleGroupQueried>
	 */
	@Transactional
	public List<UserRoleQueried> queryOthers(String username, String service) {
		List<RoleInfo> others = userRoleService.queryOthers(username, service);
		return BaseDataTransformer.transformData(others, UserRoleQueried.class);

	}
}
