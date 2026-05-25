package com.example.demo.application.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.shared.command.CreateGroupCommand;
import com.example.demo.application.shared.command.UpsertGroupCommand;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.group.aggregate.vo.GroupProfile;
import com.example.demo.domain.group.aggregate.vo.GroupScope;
import com.example.demo.domain.group.repository.GroupInfoRepository;
import com.example.demo.domain.service.GroupService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class GroupCommandService {

	private GroupService groupService;
	private GroupInfoRepository groupInfoRepository;

	/**
	 * 建立一筆群組資料
	 * 
	 * @param command {@link CreateGroupCommand}
	 */
	public void create(CreateGroupCommand command) {
		GroupScope scope = GroupScope.of(command.getService(), command.getCode());
		GroupProfile profile = GroupProfile.of(command.getName(), command.getDescription());
		GroupInfo group = GroupInfo.create(scope, profile, command.getType());
		groupInfoRepository.save(group);
	}

	/**
	 * 新增/修改多筆群組資料
	 * 
	 * @param commands {@link UpsertGroupCommand} 清單
	 */
	public void upsert(List<UpsertGroupCommand> commands) {

		// 取得 id 清單
		List<Long> ids = commands.stream().filter(command -> command.getId() != null).map(UpsertGroupCommand::getId)
				.collect(Collectors.toList());

		// 取出清單相對應資料
		List<GroupInfo> roles = groupInfoRepository.findByIdIn(ids);

		Map<Long, GroupInfo> map = roles.stream().collect(Collectors.toMap(GroupInfo::getId, Function.identity()));

		List<GroupInfo> groupList = commands.stream().map(command -> {
			// 建立 VO
			GroupScope scope = GroupScope.of(command.getService(), command.getCode());
			GroupProfile profile = GroupProfile.of(command.getName(), command.getDescription());

			// 修改
			if (!Objects.isNull(command.getId()) && !Objects.isNull(map.get(command.getId()))) {
				GroupInfo group = map.get(command.getId());
				group.update(scope, profile, command.getType(), command.getActiveFlag());
				return group;
			} else {
				// 新增
				return GroupInfo.create(scope, profile, command.getType());
			}
		}).collect(Collectors.toList());

		groupInfoRepository.saveAll(groupList);
	}

	/**
	 * 刪除多筆群組資料
	 * 
	 * @param ids 要被刪除的 id 清單
	 */
	public void delete(List<Long> ids) {
		groupService.delete(ids);
	}

}
