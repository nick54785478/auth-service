package com.example.demo.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.group.aggregate.entity.GroupRole;
import com.example.demo.domain.group.command.CreateOrUpdateGroupCommand;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.shared.detail.GroupRoleQueriedDetail;
import com.example.demo.domain.shared.summary.GroupInfoQueriedSummary;
import com.example.demo.infra.exception.ValidationException;
import com.example.demo.infra.repository.GroupInfoRepository;
import com.example.demo.infra.repository.RoleInfoRepository;
import com.example.demo.shared.enums.YesNo;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GroupService {

	private RoleInfoRepository roleInfoRepository;
	private GroupInfoRepository groupInfoRepository;

	/**
	 * 建立多筆群組資訊(僅限於前端使用 Inline-Edit)
	 * 
	 * @param commands CreateOrUpdateGroupCommand 清單
	 */
	public void createOrUpdate(List<CreateOrUpdateGroupCommand> commands) {

		// 取得 id 清單
		List<Long> ids = commands.stream().filter(command -> command.getId() != null)
				.map(CreateOrUpdateGroupCommand::getId).collect(Collectors.toList());

		// 取出清單相對應資料
		List<GroupInfo> roles = groupInfoRepository.findByIdIn(ids);

		Map<Long, GroupInfo> map = roles.stream().collect(Collectors.toMap(GroupInfo::getId, Function.identity()));

		List<GroupInfo> groupList = commands.stream().map(command -> {
			// 修改
			if (!Objects.isNull(command.getId()) && !Objects.isNull(map.get(command.getId()))) {
				GroupInfo group = map.get(command.getId());
				group.update(command);
				return group;
			} else {
				// 新增
				GroupInfo group = new GroupInfo();
				group.create(command);
				return group;
			}
		}).collect(Collectors.toList());

		groupInfoRepository.saveAll(groupList);
	}

	/**
	 * 查詢符合條件的群組資料
	 * 
	 * @param id      群組ID
	 * @param service 服務
	 * @return GroupInfoQueried
	 */
	public GroupInfoQueriedSummary getGroupInfo(Long id, String service) {
		Optional<GroupInfo> opt = groupInfoRepository.findById(id);
		if (opt.isPresent()) {
			GroupInfo group = opt.get();
			// 取得 Role Id 清單
			List<Long> roleIds = group.getRoles().stream().filter(e -> Objects.equals(e.getActiveFlag(), YesNo.Y))
					.map(GroupRole::getRoleId).collect(Collectors.toList());

			List<RoleInfo> roles = roleInfoRepository.findByIdInAndServiceAndActiveFlag(roleIds, service, YesNo.Y);

			List<GroupRoleQueriedDetail> groupRoles = roles.stream().map(role -> {
				return GroupRoleQueriedDetail.builder().id(role.getId()).service(service).code(role.getCode())
						.name(role.getName()).description(role.getDescription()).build();
			}).collect(Collectors.toList());

			return GroupInfoQueriedSummary.builder().id(id).service(service).type(group.getType()).code(group.getCode())
					.name(group.getName()).description(group.getDescription()).roles(groupRoles)
					.activeFlag(group.getActiveFlag()).build();

		} else {
			throw new ValidationException("VALIDATION_FAILED", "該群組 ID 有誤，查詢失敗");
		}
	}

	/**
	 * 查詢群組角色
	 * 
	 * @param groupId
	 * @return GroupRolesQueried
	 */
	@Transactional
	public List<GroupRoleQueriedDetail> queryRoles(Long groupId) {
		Optional<GroupInfo> opt = groupInfoRepository.findById(groupId);
		if (opt.isEmpty()) {
			return new ArrayList<>();
		} else {
			GroupInfo group = opt.get();
			List<Long> roleIds = group.getRoles().stream().map(GroupRole::getRoleId).collect(Collectors.toList());
			List<GroupRoleQueriedDetail> result = roleInfoRepository.findByIdIn(roleIds).stream().map(role -> {
				GroupRoleQueriedDetail groupRoleQueried = new GroupRoleQueriedDetail();
				groupRoleQueried.setId(role.getId());
				groupRoleQueried.setName(role.getName());
				groupRoleQueried.setCode(role.getCode());
				groupRoleQueried.setDescription(role.getDescription());
				return groupRoleQueried;
			}).collect(Collectors.toList());
			return result;
		}
	}

	/**
	 * 刪除多筆角色資料
	 * 
	 * @param ids 要被刪除的 id 清單
	 */
	public void delete(List<Long> ids) {
		List<GroupInfo> groups = groupInfoRepository.findByIdInAndActiveFlag(ids, YesNo.Y);
		groups.stream().forEach(group -> {
			group.delete();
		});
		groupInfoRepository.saveAll(groups);
	}
}
