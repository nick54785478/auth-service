package com.example.demo.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.group.aggregate.entity.GroupRole;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.aggregate.entity.RoleFunction;
import com.example.demo.domain.shared.detail.GroupsAuthQueriedDetail;
import com.example.demo.domain.shared.summary.PersonalAuthQueriedSummary;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.domain.user.aggregate.entity.UserGroup;
import com.example.demo.domain.user.aggregate.entity.UserRole;
import com.example.demo.infra.repository.FunctionInfoRepository;
import com.example.demo.infra.repository.GroupInfoRepository;
import com.example.demo.infra.repository.RoleInfoRepository;
import com.example.demo.infra.repository.UserInfoRepository;
import com.example.demo.shared.enums.YesNo;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

	private GroupInfoRepository groupRepository;
	private UserInfoRepository userInfoRepository;
	private RoleInfoRepository roleInfoRepository;
	private FunctionInfoRepository functionRepository;

	/**
	 * 查詢群組權限資料
	 * 
	 * @param userInfo 使用者資料
	 * @return GroupsAuthQueried
	 */
	public GroupsAuthQueriedDetail queryGroupsAuth(UserInfo userInfo) {
		// 取得該使用者所在群組 ID 清單
		List<Long> userGroupIds = userInfo.getGroups().stream().map(UserGroup::getGroupId).collect(Collectors.toList());
		// 透過群組 ID 清單查詢群組
		List<GroupInfo> groups = groupRepository.findByIdInAndActiveFlag(userGroupIds, YesNo.Y);
		// 取得 Role Id 清單
		List<Long> roleIds = groups.stream()
				// 使用 flatMap 將每個 group 中的 roleId 集合平鋪到一個流中。
				.flatMap(group -> group.getRoles().stream().map(GroupRole::getRoleId)).distinct()
				.collect(Collectors.toList());
		// 取得角色資料
		List<RoleInfo> roleList = roleInfoRepository.findByIdIn(roleIds);
		List<String> roles = roleList.stream().map(RoleInfo::getCode).collect(Collectors.toList());
		// 取得角色擁有的功能 ID 清單
		List<Long> functionIds = roleList.stream()
				// 使用 flatMap 將每個 role 中的 function Id 集合平鋪到一個流中。
				.flatMap(role -> role.getFunctions().stream().map(RoleFunction::getFunctionId)).distinct()
				.collect(Collectors.toList());
		// 透過功能 ID 清單搜尋該權限擁有的功能
		List<String> functions = functionRepository.findByIdInAndActiveFlag(functionIds, YesNo.Y).stream()
				.map(FunctionInfo::getCode).collect(Collectors.toList());
		return GroupsAuthQueriedDetail.builder().username(userInfo.getUsername()).email(userInfo.getEmail()).roles(roles)
				.functions(functions).build();
	}

	/**
	 * 查詢個人權限資料
	 * 
	 * @param userInfo 使用者資料
	 * @return PersonalAuthQueried
	 */
	public PersonalAuthQueriedSummary queryPersonalAuth(UserInfo userInfo) {
		// 取出該使用者所具備的角色 ID 清單
		List<Long> roleIds = userInfo.getRoles().stream().map(UserRole::getRoleId).collect(Collectors.toList());
		// 透過角色 ID 清單找出所屬角色
		List<RoleInfo> roleList = roleInfoRepository.findByIdIn(roleIds);
		List<Long> functionIds = roleList.stream()
				// 使用 flatMap 將每個 role 中的 functionId 集合平鋪到一個流中。
				.flatMap(role -> role.getFunctions().stream().map(RoleFunction::getFunctionId)).distinct()
				.collect(Collectors.toList());

		List<String> roles = roleList.stream().map(RoleInfo::getCode).collect(Collectors.toList());

		List<String> functions = functionRepository.findByIdInAndActiveFlag(functionIds, YesNo.Y).stream()
				.map(FunctionInfo::getCode).collect(Collectors.toList());
		return PersonalAuthQueriedSummary.builder().username(userInfo.getUsername()).email(userInfo.getEmail()).roles(roles)
				.functions(functions).build();
	}

	/**
	 * 查詢維護頁面權限清單
	 * 
	 * @param username 使用者名稱
	 */
	@Transactional
	public List<String> getMaintainPermissions(String username) {

		UserInfo userInfo = userInfoRepository.findByUsername(username);
		// 取出該使用者所具備的角色 ID 清單
		List<Long> roleIds = userInfo.getRoles().stream().map(UserRole::getRoleId).collect(Collectors.toList());
		// 透過角色 ID 清單找出所屬角色
		List<RoleInfo> roleList = roleInfoRepository.findByIdIn(roleIds);

		List<String> roles = roleList.stream().map(RoleInfo::getCode).filter(e -> StringUtils.equals(e, "ADMIN"))
				.collect(Collectors.toList());
		// Admin 直接看全部
		if (roles.contains("ADMIN")) {
			return functionRepository.findByTypeAndActiveFlag("MAINTAIN", YesNo.Y).stream().map(FunctionInfo::getCode)
					.collect(Collectors.toList());
		}

		List<Long> functionIds = roleList.stream()
				// 使用 flatMap 將每個 role 中的 functionId 集合平鋪到一個流中。
				.flatMap(role -> role.getFunctions().stream().map(RoleFunction::getFunctionId)).distinct()
				.collect(Collectors.toList());
		return functionRepository.findByIdInAndTypeAndActiveFlag(functionIds, "MAINTAIN", YesNo.Y).stream()
				.map(FunctionInfo::getCode).collect(Collectors.toList());

	}

}
