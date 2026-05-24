package com.example.demo.application.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.application.shared.dto.UserGroupQueried;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.group.aggregate.vo.GroupProfile;
import com.example.demo.domain.group.aggregate.vo.GroupScope;

@Component
public class UserGroupAssembler {

	/**
	 * 將領域實體 (GroupInfo) 轉換為使用者群組查詢結果 DTO
	 * 
	 * @param group 群組資料 (Aggregate Root)
	 * @return {@link UserGroupQueried} 轉換後的 DTO；若傳入 null 則回傳 null
	 */
	public UserGroupQueried transform(GroupInfo group) {
		if (group == null) {
			return null;
		}

		// 1. 提取 Value Objects
		GroupScope scope = group.getScope();
		GroupProfile profile = group.getProfile();

		// 2. 防禦性取值，避免資料庫歷史髒資料導致 NullPointerException
		String service = (scope != null) ? scope.getService() : null;
		String code = (scope != null) ? scope.getCode() : null;
		String name = (profile != null) ? profile.getName() : null;
		String description = (profile != null) ? profile.getDescription() : null;

		// 3. 使用 Builder 安全地組裝 DTO
		return UserGroupQueried.builder().id(group.getId()).service(service).code(code).name(name).type(group.getType())
				.description(description).activeFlag(group.getActiveFlag()).build();
	}

	/**
	 * 將領域實體清單 (List<GroupInfo>) 轉換為使用者群組查詢結果清單
	 * 
	 * @param groups 群組資料 (Aggregate Root) 清單
	 * @return {@link UserGroupQueried} 清單；若傳入空值或空集合則回傳空 List
	 */
	public List<UserGroupQueried> transformList(List<GroupInfo> groups) {
		if (groups == null || groups.isEmpty()) {
			return List.of(); // 回傳不可變的空集合，保護呼叫端
		}

		return groups.stream().map(this::transform).collect(Collectors.toList());
	}
}