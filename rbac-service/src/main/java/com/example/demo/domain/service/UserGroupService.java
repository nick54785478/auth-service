package com.example.demo.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.group.repository.GroupInfoRepository;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.domain.user.aggregate.entity.UserGroup;
import com.example.demo.domain.user.repository.UserInfoRepository;
import com.example.demo.infra.exception.ValidationException;
import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserGroupService {

	private UserInfoRepository userInfoRepository;
	private GroupInfoRepository groupInfoRepository;

	/**
	 * 取得特定使用者所在的群組資料
	 * 
	 * @param username 使用者帳號
	 * @param service  Service
	 * @return List<GroupInfo>
	 */
	public List<GroupInfo> queryGroups(String username, String service) {
		// 查出 User Aggregate，若無則拋出例外 (適應 Optional 調整)
		UserInfo user = userInfoRepository.findByUsername(username);
		if (user == null) {
			throw new ValidationException("VALIDATE_FAILED", "該使用者帳號有誤，查詢失敗");
		}

		// 篩選出該使用者「啟用中 (YesNo.Y)」的群組關聯
		List<Long> activeGroupIds = user.getGroups().stream().filter(e -> e.getActiveFlag() == YesNo.Y)
				.map(UserGroup::getGroupId).collect(Collectors.toList());

		if (activeGroupIds.isEmpty()) {
			return new ArrayList<>();
		}

		// 透過 ID 取得 Group 資料，並交由 DB 進行 Scope 與狀態過濾
		return groupInfoRepository.findByIdInAndScopeServiceAndActiveFlag(activeGroupIds, service, YesNo.Y);

	}

	/**
	 * 查詢不屬於該使用者的群組 (已透過 service 過濾)
	 * 
	 * @param username 使用者帳號
	 * @param service  Service
	 * @return List<GroupInfo>
	 */
	public List<GroupInfo> getOtherUserGroups(String username, String service) {
		// 查出 User Aggregate，若無則拋出例外 (適應 Optional 調整)
		UserInfo user = userInfoRepository.findByUsername(username);
		if (user == null) {
			throw new ValidationException("VALIDATE_FAILED", "該使用者帳號有誤，查詢失敗");
		}

		// 篩選出該使用者「目前已擁有，且關聯有效 (Y)」的群組 ID 清單
		List<Long> activeAssignedGroupIds = user.getGroups().stream().filter(e -> e.getActiveFlag() == YesNo.Y)
				.map(UserGroup::getGroupId).collect(Collectors.toList());

		// 查出該 Service 下系統中「所有啟用中」的群組資料
		List<GroupInfo> allActiveGroupsInService = groupInfoRepository.findByScopeServiceAndActiveFlag(service,
				YesNo.Y);

		// 邏輯簡化：全部有效群組 - 使用者已擁有的有效群組 = 使用者不具備的其他群組
		return allActiveGroupsInService.stream().filter(g -> !activeAssignedGroupIds.contains(g.getId()))
				.collect(Collectors.toList());
	}

	/**
	 * 將使用者加入特定群組
	 * 
	 * @param service  服務
	 * @param username 使用者帳號
	 * @param groupIds 群組 ID 清單
	 */
	public void update(String service, String username, List<Long> groupIds) {
		UserInfo userInfo = userInfoRepository.findByUsername(username);

		List<GroupInfo> groups = groupInfoRepository.findByIdInAndActiveFlag(groupIds, YesNo.Y);

		// 處理要被更新的 Group 資料
		List<UserGroup> userGroups = this.processUpdatedGroupData(service, userInfo, groups);

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
				.filter(group -> !StringUtils.equals(group.getScope().getService(), service))
				.collect(Collectors.toList());

		groupList.addAll(groups);
		groupList.addAll(filtered);

		// 將角色資料轉為 UserGroup
		return groupList.stream().map(group -> {
			UserGroup userGroup = new UserGroup();
			userGroup.create(group.getId(), userInfo.getId());
			return userGroup;
		}).collect(Collectors.toList());
	}

}
