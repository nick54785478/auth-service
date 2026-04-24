package com.example.demo.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.group.aggregate.entity.GroupRole;
import com.example.demo.domain.group.command.UpdateGroupRolesCommand;
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
	 * 查詢該群組內部不存在的其他角色(要透過 service 過濾，不然會有其他服務的角色)
	 * 
	 * @param id      群組ID
	 * @param service 服務
	 * @return List<GroupRoleQueried>
	 */
	public List<RoleInfo> queryOthers(Long id, String service) {
		Optional<GroupInfo> opt = groupInfoRepository.findById(id);
		if (opt.isPresent()) {
			GroupInfo group = opt.get();

			// 篩選出該角色有的 Role ID 清單
			List<Long> existingIds = group.getRoles().stream().map(GroupRole::getRoleId).collect(Collectors.toList());

			// 取得與該 Service 相關的角色資料
			List<RoleInfo> roles = roleInfoRepository.findByServiceAndActiveFlag(service, YesNo.Y);
			
			
			// 過濾出該群組所沒有的角色資料
			List<RoleInfo> filtered = roles.stream().filter(e -> !existingIds.contains(e.getId()))
					.collect(Collectors.toList());

			// 過濾出該使用者角色 ActiveFlag = 'N' 的資料
			List<Long> inactiveRelatedIds = group.getRoles().stream()
					.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.N.getValue()))
					.map(GroupRole::getRoleId).collect(Collectors.toList());

			// 過濾出該使用者資料但失效的資料 activeFlag = 'N'
			List<RoleInfo> inactiveRelated = roles.stream().filter(e -> inactiveRelatedIds.contains(e.getId()))
					.collect(Collectors.toList());

			// 合併兩者
			filtered.addAll(inactiveRelated);
			
			return filtered;
					

		} else {
			throw new ValidationException("VALIDATION_FAILED", "該群組 ID 有誤，查詢失敗");
		}

	}

	/**
	 * 更新群組角色
	 * 
	 * @param command
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
