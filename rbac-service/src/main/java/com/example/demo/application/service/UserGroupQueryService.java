package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.assembler.UserGroupAssembler;
import com.example.demo.application.shared.dto.UserGroupQueried;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.service.UserGroupService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserGroupQueryService {

	private UserGroupAssembler assembler;
	private UserGroupService userGroupService;

	/**
	 * 取得特定使用者所在的群組資料
	 * 
	 * @param username 使用者帳號
	 * @param service  服務
	 * @return List<UserGroupQueried>
	 */
	@Transactional
	public List<UserGroupQueried> queryGroups(String username, String service) {
		List<GroupInfo> groups = userGroupService.queryGroups(username, service);
		return assembler.transformList(groups);
	}

	/**
	 * 查詢該群組內部不存在的其他群組
	 * 
	 * @param username 使用者帳號
	 * @param service  服務
	 * @return List<UserGroupQueried>
	 */
	@Transactional
	public List<UserGroupQueried> getOtherUserGroups(String username, String service) {
		List<GroupInfo> others = userGroupService.getOtherUserGroups(username, service);
		return assembler.transformList(others);
	}
}
