package com.example.demo.application.assembler;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.application.shared.dto.GroupDetailQueried;
import com.example.demo.application.shared.dto.RoleDetailQueried;
import com.example.demo.application.shared.dto.UserDetailsQueried;
import com.example.demo.application.shared.dto.UserInfoQueried;
import com.example.demo.application.shared.dto.UserOptionQueried;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.group.aggregate.vo.GroupProfile;
import com.example.demo.domain.group.aggregate.vo.GroupScope;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.aggregate.vo.RoleProfile;
import com.example.demo.domain.role.aggregate.vo.RoleScope;
import com.example.demo.domain.shared.summary.UserInfoDetailsQueriedSummary;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.domain.user.aggregate.vo.UserProfile;

@Component
public class UserAssembler {

	/**
	 * 轉換 User Option 資料
	 * 
	 * @param userInfo 使用者資訊
	 * @return {@link UserOptionQueried}
	 */
	public UserOptionQueried transformUserOption(UserInfo user) {
		if (user == null) {
			return null;
		}
		return new UserOptionQueried(user.getId(), user.getUsername(), user.getProfile().getName());
	}

	/**
	 * 轉換 User Option 資料
	 * 
	 * @param users 使用者資訊清單
	 * @return {@link UserOptionQueried} 清單
	 */
	public List<UserOptionQueried> transformUserOptions(List<UserInfo> users) {
		if (users == null || users.isEmpty()) {
			return List.of();
		}
		return users.stream().map(this::transformUserOption).collect(Collectors.toList());
	}

	/**
	 * 轉換 User 詳細資料
	 * 
	 * @param summary {@link UserInfoDetailsQueriedSummary}
	 * @return UserDetailsQueried
	 */
	public UserDetailsQueried transformUserDetails(UserInfoDetailsQueriedSummary summary) {
		return UserDetailsQueried.builder().id(summary.getId()).name(summary.getName())
				.nationalIdNo(summary.getNationalIdNo()).birthday(summary.getBirthday()).address(summary.getAddress())
				.username(summary.getUsername()).email(summary.getEmail()).activeFlag(summary.getActiveFlag())
				.groups(this.transformGroups(summary.getGroups())).roles(transformRoles(summary.getRoles()))
				.functions(summary.getFunctions()).build();
	}

	/**
	 * 轉換 User 資訊
	 * 
	 * @param userInfo {@link UserInfo}
	 * @return {@link UserInfoQueried}
	 */
	public UserInfoQueried transformUser(UserInfo userInfo) {
		if (userInfo == null) {
			return null;
		}

		UserProfile profile = userInfo.getProfile();
		return new UserInfoQueried(userInfo.getId(), profile.getName(), profile.getEmail(), userInfo.getUsername(),
				profile.getAddress(), profile.getNationalIdNo(), profile.getBirthday());
	}

	/**
	 * 轉換 Group 資料
	 * 
	 * @param group {@link GroupInfo} 資料
	 * @return {@link GroupDetailQueried}
	 */
	private List<GroupDetailQueried> transformGroups(List<GroupInfo> groups) {
		if (groups == null || groups.isEmpty()) {
			return List.of();
		}
		return groups.stream().map(group -> {
			GroupScope scope = group.getScope();
			GroupProfile profile = group.getProfile();
			return new GroupDetailQueried(group.getId(), scope.getService(), group.getType(), scope.getCode(),
					profile.getName(), profile.getDescription(), group.getActiveFlag());
		}).collect(Collectors.toList());
	}

	/**
	 * 轉換 Role 清單資料
	 * 
	 * @param roles 角色資料清單
	 * @return {@link RoleDetailQueried} 清單
	 */
	private List<RoleDetailQueried> transformRoles(List<RoleInfo> roles) {
		if (roles == null || roles.isEmpty()) {
			return List.of(); // 回傳空集合，避免回傳 null 造成呼叫端麻煩
		}
		return roles.stream().map(role -> {
			if (role == null) {
				return null;
			}

			RoleScope scope = role.getScope();
			RoleProfile profile = role.getProfile();

			// 使用三元運算子或 Optional 防禦內嵌物件為空的情況 (視業務嚴格程度而定)
			String service = scope != null ? scope.getService() : null;
			String code = scope != null ? scope.getCode() : null;
			String name = profile != null ? profile.getName() : null;
			String description = profile != null ? profile.getDescription() : null;

			return new RoleDetailQueried(role.getId(), service, code, name, role.getType(), description,
					role.getActiveFlag());
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}
}
