package com.example.demo.application.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.application.shared.dto.GroupInfoQueried;
import com.example.demo.application.shared.dto.GroupOptionQueried;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.group.aggregate.vo.GroupProfile;
import com.example.demo.domain.group.aggregate.vo.GroupScope;

/**
 * 群組資料轉換器 (Group Assembler)
 */
@Component
public class GroupAssembler {

	/**
	 * 轉換 Group 資料清單
	 * 
	 * @param groups {@link GroupInfo} 資料清單
	 * @return {@link GroupOptionQueried} 清單
	 */
	public List<GroupOptionQueried> transformGroupOptions(List<GroupInfo> groups) {
		return groups.stream().map(this::transformGroupOption).collect(Collectors.toList());
	}

	/**
	 * 轉換 Group Option 資料
	 * 
	 * @param group {@link GroupInfo} 資料
	 * @return {@link GroupOptionQueried}
	 */
	public GroupOptionQueried transformGroupOption(GroupInfo group) {
		if (group == null) {
			return null;
		}
		String code = group.getScope().getCode();
		String name = group.getProfile().getName();
		return new GroupOptionQueried(group.getId(), code, name);
	}

	/**
	 * 轉換 Group 資料清單
	 * 
	 * @param groups {@link GroupInfo} 資料清單
	 * @return {@link GroupOptionQueried} 清單
	 */
	public List<GroupInfoQueried> transformGroups(List<GroupInfo> groups) {
		return groups.stream().map(this::transformGroup).collect(Collectors.toList());
	}

	/**
	 * 轉換 Group 資料
	 * 
	 * @param group {@link GroupInfo} 資料
	 * @return {@link GroupInfoQueried}
	 */
	public GroupInfoQueried transformGroup(GroupInfo group) {
		if (group == null) {
			return null;
		}
		GroupScope scope = group.getScope();
		GroupProfile profile = group.getProfile();
		return new GroupInfoQueried(group.getId(), scope.getService(), group.getType(), scope.getCode(),
				profile.getName(), profile.getDescription(), group.getActiveFlag());
	}
}
