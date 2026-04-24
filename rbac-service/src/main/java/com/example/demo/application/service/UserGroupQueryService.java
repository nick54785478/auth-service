package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.shared.dto.UserGroupQueried;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.service.UserGroupService;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserGroupQueryService {

	private UserGroupService userGroupService;

	/**
	 * 取得特定使用者所在的 Role 資料
	 * 
	 * @param username 使用者帳號
	 * @param service  服務
	 * @return List<UserGroupQueried>
	 */
	@Transactional
	public List<UserGroupQueried> queryGroups(String username, String service) {
		List<GroupInfo> groups = userGroupService.queryGroups(username, service);
		return BaseDataTransformer.transformData(groups, UserGroupQueried.class);
	}

	/**
	 * 查詢該群組內部不存在的其他角色
	 * 
	 * @param username 使用者帳號
	 * @param service  服務
	 * @return List<GroupRoleQueried> 群組角色清單
	 */
	@Transactional
	public List<UserGroupQueried> queryOthers(String username, String service) {
		List<GroupInfo> others = userGroupService.queryOthers(username, service);
		return BaseDataTransformer.transformData(others, UserGroupQueried.class);
	}
}
