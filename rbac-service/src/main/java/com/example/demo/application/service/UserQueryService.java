package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.assembler.UserAssembler;
import com.example.demo.application.shared.dto.UserDetailsQueried;
import com.example.demo.application.shared.dto.UserGroupQueried;
import com.example.demo.application.shared.dto.UserInfoQueried;
import com.example.demo.application.shared.dto.UserRoleQueried;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.service.UserService;
import com.example.demo.domain.shared.summary.UserInfoDetailsQueriedSummary;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.infra.persistence.UserInfoPersistence;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserQueryService {

	private UserAssembler assembler;
	private UserService userService;
	private UserInfoPersistence userRepository;

	/**
	 * 取得特定使用者所在的 Group 資料
	 * 
	 * @param username 使用者帳號
	 * @return List<UserGroupQueried> 使用者群組清單
	 */
	@Transactional
	public List<UserGroupQueried> queryGroups(String username) {
		List<GroupInfo> groups = userService.queryGroups(username);
		return BaseDataTransformer.transformData(groups, UserGroupQueried.class);
	}

	/**
	 * 取得特定使用者所在的 Role 資料
	 * 
	 * @param username 使用者帳號
	 * @return List<UserRoleQueried>
	 */
	@Transactional(readOnly = true)
	public List<UserRoleQueried> queryRoles(String username) {
		List<RoleInfo> roles = userService.queryRoles(username);
		log.debug("roles: {}", roles);
		return BaseDataTransformer.transformData(roles, UserRoleQueried.class);
	}

	/**
	 * 查詢該使用者資訊
	 * 
	 * @param username 使用者帳號
	 * @return UserInfoQueried
	 */
	@Transactional(readOnly = true)
	public UserInfoQueried query(String username) {
		UserInfo userInfo = userRepository.findByUsername(username);
		return assembler.transformUser(userInfo);
	}

	/**
	 * 取得使用者詳細資訊
	 * 
	 * @param username 使用者名稱
	 * @param service  服務
	 * @return UserInfoQueried
	 */
	@Transactional(readOnly = true)
	public UserDetailsQueried getUserDetails(String username, String service) {
		UserInfoDetailsQueriedSummary userDetails = userService.getUserDetails(username, service);
		return assembler.transformUserDetails(userDetails);
	}

}
