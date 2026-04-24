package com.example.demo.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.domain.user.aggregate.entity.UserRole;
import com.example.demo.domain.user.command.UpdateUserRolesCommand;
import com.example.demo.infra.exception.ValidationException;
import com.example.demo.infra.repository.RoleInfoRepository;
import com.example.demo.infra.repository.UserInfoRepository;
import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserRoleService {

	private RoleInfoRepository roleInfoRepository;
	private UserInfoRepository userInfoRepository;

	/**
	 * 查詢不屬於該使用者的其他角色
	 * 
	 * @param username 使用者名稱
	 * @param service  服務
	 * @return List<UserRoleGroupQueried>
	 */
	@Transactional
	public List<RoleInfo> queryOthers(String username, String service) {
		UserInfo userInfo = userInfoRepository.findByUsername(username);
		if (!Objects.isNull(userInfo)) {
			// 篩選出該使用者有的 角色 ID 清單
			List<Long> existingIds = userInfo.getRoles().stream().map(UserRole::getRoleId).collect(Collectors.toList());

			// 查出該使用者在某服務的角色資料清單
			List<RoleInfo> roles = roleInfoRepository.findByServiceAndActiveFlag(service, YesNo.Y);

			// 過濾出該使用者所沒有的角色資料
			List<RoleInfo> filtered = roles.stream().filter(e -> !existingIds.contains(e.getId()))
					.collect(Collectors.toList());

			// 過濾出該使用者角色 ActiveFlag = 'N' 的資料
			List<Long> inactiveRelatedIds = userInfo.getRoles().stream()
					.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.N.getValue()))
					.map(UserRole::getRoleId).collect(Collectors.toList());

			// 過濾出該使用者資料但失效的資料 activeFlag = 'N'
			List<RoleInfo> inactiveRelated = roles.stream().filter(e -> inactiveRelatedIds.contains(e.getId()))
					.collect(Collectors.toList());

			// 合併兩者
			filtered.addAll(inactiveRelated);
			
			return filtered;		
		} else {
			throw new ValidationException("VALIDATION_FAILED", "該角色 ID 有誤，查詢失敗");
		}

	}

	/**
	 * 更新使用者角色權限
	 * 
	 * @param command
	 */
	public void update(UpdateUserRolesCommand command) {
		UserInfo userInfo = userInfoRepository.findByUsername(command.getUsername());

		// 取出所有要被更新的角色
		List<RoleInfo> roles = roleInfoRepository.findByIdInAndActiveFlag(command.getRoleIds(), YesNo.Y);

		List<UserRole> userRoles = this.processUpdatedRoleData(command.getService(), userInfo, roles);

		// 更新群組資料
		userInfo.updateRoles(userRoles);
		userInfoRepository.save(userInfo);
	}

	/**
	 * 處理要被更新的 Role 資料
	 * 
	 * @param service    服務
	 * @param userInfo   使用者資料
	 * @param roles      要被更新的角色清單
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
		List<RoleInfo> filtered = otherRoles.stream().filter(role -> !StringUtils.equals(role.getService(), service))
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

	/**
	 * 取得特定使用者的角色資料
	 * 
	 * @param username 使用者帳號
	 * @param service  服務
	 * @return List<UserRoleQueried>
	 */
	public List<RoleInfo> queryRoles(String username, String service) {
		UserInfo user = userInfoRepository.findByUsername(username);
		// 取得該使用者的 RoleId 清單
		List<Long> roleIds = user.getRoles().stream()
				// 過濾 UserRole 的 activeFlag = 'N' 者
				.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.Y.getValue()))
				.map(UserRole::getRoleId).collect(Collectors.toList());

		return roleInfoRepository.findByIdInAndServiceAndActiveFlag(roleIds, service, YesNo.Y);

	}
}
