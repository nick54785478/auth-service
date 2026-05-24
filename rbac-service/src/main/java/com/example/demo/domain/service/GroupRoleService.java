package com.example.demo.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.application.shared.command.UpdateGroupRolesCommand;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.group.aggregate.entity.GroupRole;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.infra.exception.ValidationException;
import com.example.demo.infra.repository.GroupInfoRepository;
import com.example.demo.infra.repository.RoleInfoRepository;
import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GroupRoleService {

	private RoleInfoRepository roleInfoRepository;
	private GroupInfoRepository groupInfoRepository;

	/**
	 * 查詢群組角色
	 * 
	 * @param id      Group id
	 * @param service Service
	 * @return List<RoleInfo>
	 */
	public List<RoleInfo> getGroupRoles(Long id, String service) {
		return groupInfoRepository.findById(id).map(group -> {
			// 1. 篩選出該群組中「啟用中 (YesNo.Y)」的 GroupRole 關聯，並提取 Role ID
			List<Long> activeRoleIds = group.getRoles().stream().filter(e -> e.getActiveFlag() == YesNo.Y)
					.map(GroupRole::getRoleId).collect(Collectors.toList());

			if (activeRoleIds.isEmpty()) {
				return new ArrayList<RoleInfo>();
			}

			// 2. 將 ID In、Service (Scope) 以及 ActiveFlag 條件統包交給 DB 查詢
			// 避免將所有 Role 查回 Java 記憶體後再跑 filter
			return roleInfoRepository.findByIdInAndScopeServiceAndActiveFlag(activeRoleIds, service, YesNo.Y);
		}).orElse(new ArrayList<>()); // 找不到 Group 時回傳空陣列
	}

	/**
	 * 查詢該群組內部不存在的其他角色 (已透過 service 過濾)
	 * 
	 * @param id      Group id
	 * @param service Service
	 * @return List<RoleInfo>
	 */
	public List<RoleInfo> getOtherGroupRoles(Long id, String service) {
		// 1. 查出 Group Aggregate，若無則拋出例外
		GroupInfo group = groupInfoRepository.findById(id)
				.orElseThrow(() -> new ValidationException("VALIDATE_FAILED", "該群組 ID 有誤，查詢失敗"));

		// 2. 篩選出該群組「目前已擁有，且關聯有效 (Y)」的 Role ID 清單
		List<Long> activeAssignedRoleIds = group.getRoles().stream().filter(e -> e.getActiveFlag() == YesNo.Y)
				.map(GroupRole::getRoleId).collect(Collectors.toList());

		// 3. 查出該 Service 下，系統中「所有啟用中」的 Role 資料
		List<RoleInfo> allActiveRolesInService = roleInfoRepository.findByScopeServiceAndActiveFlag(service, YesNo.Y);

		// 4. 邏輯簡化：系統有效角色 - 群組已擁有的有效角色 = 群組不具備的其他角色
		return allActiveRolesInService.stream().filter(r -> !activeAssignedRoleIds.contains(r.getId()))
				.collect(Collectors.toList());
	}

	/**
	 * 更新群組角色
	 * 
	 * @param command {@link UpdateGroupRolesCommand}
	 */
	public void update(UpdateGroupRolesCommand command) {
		// 透過 Role id 清單找出 Role 資料
		List<RoleInfo> roleList = roleInfoRepository.findByIdInAndActiveFlag(command.getRoleIds(), YesNo.Y);
		// 透過 group id 找到 Group 資料
		groupInfoRepository.findById(command.getGroupId()).ifPresent(group -> {
			List<GroupRole> groupRoles = roleList.stream().map(role -> {
				GroupRole groupRole = new GroupRole();
				groupRole.create(group.getId(), role.getId());
				return groupRole;
			}).collect(Collectors.toList());

			// 變更群組角色
			group.updateRoles(groupRoles);
			groupInfoRepository.save(group);
		});
	}
}
