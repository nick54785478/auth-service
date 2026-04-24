package com.example.demo.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.domain.user.aggregate.entity.UserGroup;
import com.example.demo.domain.user.command.UpdateUserGroupsCommand;
import com.example.demo.infra.exception.ValidationException;
import com.example.demo.infra.repository.GroupInfoRepository;
import com.example.demo.infra.repository.UserInfoRepository;
import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserGroupService {

	private UserInfoRepository userInfoRepository;
	private GroupInfoRepository groupInfoRepository;

	/**
	 * 查詢不屬於該使用者的群組
	 * 
	 * @param username 使用者帳號
	 * @param service  服務
	 * @return List<UserGroupQueried>
	 */
	public List<GroupInfo> queryOthers(String username, String service) {
		UserInfo userInfo = userInfoRepository.findByUsername(username);

		if (!Objects.isNull(userInfo)) {

			// 篩選出該使用者有的 群組 ID 清單
			List<Long> existingIds = userInfo.getGroups().stream().map(UserGroup::getGroupId)
					.collect(Collectors.toList());

			List<GroupInfo> groups = groupInfoRepository.findByServiceAndActiveFlag(service, YesNo.Y);

			// 過濾出該使用者沒有的群組資料
			List<GroupInfo> filtered = groups.stream().filter(e -> !existingIds.contains(e.getId()))
					.collect(Collectors.toList());

			// 過濾出該使用者角色 ActiveFlag = 'N' 的資料
			List<Long> inactiveRelatedIds = userInfo.getGroups().stream()
					.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.N.getValue()))
					.map(UserGroup::getGroupId).collect(Collectors.toList());

			// 過濾出該使用者資料但失效的資料 activeFlag = 'N'
			List<GroupInfo> inactiveRelated = groups.stream().filter(e -> inactiveRelatedIds.contains(e.getId()))
					.collect(Collectors.toList());

			// 合併兩者
			filtered.addAll(inactiveRelated);
			
			return filtered;
			
		} else {
			throw new ValidationException("VALIDATION_FAILED", "該群組 ID 有誤，查詢失敗");
		}
	}

	/**
	 * 將使用者加入特定群組
	 * 
	 * @param command
	 * @return UserGroupAdded
	 */
	public void update(UpdateUserGroupsCommand command) {
		UserInfo userInfo = userInfoRepository.findByUsername(command.getUsername());
		
		List<GroupInfo> groups = groupInfoRepository.findByIdInAndActiveFlag(command.getGroupIds(), YesNo.Y);

		// 處理要被更新的 Group 資料
		List<UserGroup> userGroups = this.processUpdatedGroupData(command.getService(), userInfo, groups);

		// 更新群組資料
		userInfo.updateGroups(userGroups);
		userInfoRepository.save(userInfo);
	}

	/**
	 * 處理要被更新的 Group 資料
	 * 
	 * @param service  服務
	 * @param userInfo 使用者資料
	 * @param roles    要被更新的群組清單
	 */
	private List<UserGroup> processUpdatedGroupData(String service, UserInfo userInfo, List<GroupInfo> groups) {
		List<GroupInfo> groupList = new ArrayList<>();

		// 取出使用者目前的群組
		List<Long> currentGroupIds = userInfo.getGroups().stream()
				.filter(group -> Objects.equals(group.getActiveFlag(), YesNo.Y)).map(UserGroup::getGroupId).distinct()
				.collect(Collectors.toList());

		// 查出原先屬於我的群組
		List<GroupInfo> otherGroups = groupInfoRepository.findByIdInAndActiveFlag(currentGroupIds, YesNo.Y);

		// 過濾出不屬於該服務的群組清單
		List<GroupInfo> filtered = otherGroups.stream()
				.filter(group -> !StringUtils.equals(group.getService(), service)).collect(Collectors.toList());

		groupList.addAll(groups);
		groupList.addAll(filtered);

		// 將角色資料轉為 UserGroup
		return groupList.stream().map(group -> {
			UserGroup userGroup = new UserGroup();
			userGroup.create(group.getId(), userInfo.getId());
			return userGroup;
		}).collect(Collectors.toList());
	}

	/**
	 * 取得特定使用者所在的群組資料
	 * 
	 * @param username 使用者帳號
	 * @return List<UserGroupQueried>
	 */
	public List<GroupInfo> queryGroups(String username, String service) {
		UserInfo userInfo = userInfoRepository.findByUsername(username);
		// 取得 User Group 的 GroupId
		List<Long> groupIds = userInfo.getGroups().stream()
				.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.Y.getValue()))
				.map(UserGroup::getGroupId).collect(Collectors.toList());
		// 透過 ID 取得 Group 資料
		return groupInfoRepository.findByIdInAndServiceAndActiveFlag(groupIds, service, YesNo.Y);
	}
}
