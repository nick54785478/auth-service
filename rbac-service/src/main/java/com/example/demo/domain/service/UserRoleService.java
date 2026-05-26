package com.example.demo.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.repository.RoleInfoRepository;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.domain.user.aggregate.entity.UserRole;
import com.example.demo.domain.user.repository.UserInfoRepository;
import com.example.demo.infra.exception.ValidationException;
import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserRoleService {

	private RoleInfoRepository roleInfoRepository;
	private UserInfoRepository userInfoRepository;

	/**
	 * 取得特定使用者的角色資料
	 * 
	 * @param username 使用者帳號
	 * @param service  服務
	 * @return List<RoleInfo>
	 */
	public List<RoleInfo> getUserRoles(String username, String service) {
		// 1. 查出 User Aggregate，若無則拋出例外 (適應 Optional，並修正錯誤訊息)
		UserInfo user = userInfoRepository.findByUsername(username);
		if (user == null) {
			throw new ValidationException("VALIDATE_FAILED", "該使用者帳號有誤，查詢失敗");
		}

		// 1. 篩選出該使用者「啟用中 (YesNo.Y)」的 UserRole 關聯，並提取 Role ID
		List<Long> activeRoleIds = user.getRoles().stream().filter(e -> e.getActiveFlag() == YesNo.Y)
				.map(UserRole::getRoleId).collect(Collectors.toList());

		if (activeRoleIds.isEmpty()) {
			return new ArrayList<>();
		}

		// 2. 將 ID In、Service (Scope) 以及 ActiveFlag 條件統包交給 DB 查詢
		return roleInfoRepository.findByIdInAndScopeServiceAndActiveFlag(activeRoleIds, service, YesNo.Y);

	}

	/**
	 * 查詢不屬於該使用者的其他角色
	 * 
	 * @param username 使用者名稱
	 * @param service  服務
	 * @return List<RoleInfo>
	 */
	public List<RoleInfo> getOtherRoles(String username, String service) {
		// 1. 查出 User Aggregate，若無則拋出例外 (適應 Optional，並修正錯誤訊息)
		UserInfo user = userInfoRepository.findByUsername(username);
		if (user == null) {
			throw new ValidationException("VALIDATE_FAILED", "該使用者帳號有誤，查詢失敗");
		}

		// 2. 篩選出該使用者「目前已擁有，且關聯有效 (Y)」的 Role ID 清單
		List<Long> activeAssignedRoleIds = user.getRoles().stream().filter(e -> e.getActiveFlag() == YesNo.Y)
				.map(UserRole::getRoleId).collect(Collectors.toList());

		// 3. 查出該 Service 下系統中「所有啟用中」的角色資料
		List<RoleInfo> allActiveRolesInService = roleInfoRepository.findByScopeServiceAndActiveFlag(service, YesNo.Y);

		// 4. 邏輯簡化：全部有效角色 - 使用者已擁有的有效角色 = 使用者不具備的其他角色
		return allActiveRolesInService.stream().filter(r -> !activeAssignedRoleIds.contains(r.getId()))
				.collect(Collectors.toList());
	}

	/**
	 * 更新使用者角色權限
	 * 
	 * @param service  服務
	 * @param username 使用者帳號
	 * @param roleIds  角色 ID 清單
	 */
	public void update(String service, String username, List<Long> roleIds) {
		UserInfo userInfo = userInfoRepository.findByUsername(username);

		// 取出所有要被更新的角色
		List<RoleInfo> roles = roleInfoRepository.findByIdInAndActiveFlag(roleIds, YesNo.Y);

		List<UserRole> userRoles = this.processUpdatedRoleData(service, userInfo, roles);

		// 更新群組資料
		userInfo.updateRoles(userRoles);
		userInfoRepository.save(userInfo);
	}

	/**
	 * 處理要被更新的 Role 資料
	 * 
	 * @param service  服務
	 * @param userInfo 使用者資料
	 * @param roles    要被更新的角色清單
	 */
	private List<UserRole> processUpdatedRoleData(String service, UserInfo userInfo, List<RoleInfo> roles) {
		List<RoleInfo> roleList = new ArrayList<>();

		// 取出使用者目前的角色
		List<Long> currentRoleIds = userInfo.getRoles().stream()
				.filter(role -> Objects.equals(role.getActiveFlag(), YesNo.Y)).map(UserRole::getRoleId).distinct()
				.collect(Collectors.toList());

		// 查出原先屬於我的角色
		List<RoleInfo> otherRoles = roleInfoRepository.findByIdInAndActiveFlag(currentRoleIds, YesNo.Y);

		// 過濾出不屬於該服務的角色清單
		List<RoleInfo> filtered = otherRoles.stream()
				.filter(role -> !StringUtils.equals(role.getScope().getService(), service))
				.collect(Collectors.toList());

		roleList.addAll(roles);
		roleList.addAll(filtered);

		// 將角色資料轉為 UserRole
		return roleList.stream().map(role -> {
			UserRole userRole = new UserRole();
			userRole.create(userInfo.getId(), role.getId());
			return userRole;
		}).collect(Collectors.toList());
	}

}
