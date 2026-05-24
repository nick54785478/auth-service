package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.assembler.GroupRoleAssembler;
import com.example.demo.application.shared.dto.GroupRoleQueried;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.service.GroupRoleService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class GroupRoleQueryService {

	private GroupRoleAssembler assembler;
	private GroupRoleService groupRoleService;

	/**
	 * 查詢其他(不屬於該群組)的角色
	 * 
	 * @param id      Group id
	 * @param service Service
	 * @return List<GroupRoleQueried>
	 */
	@Transactional(readOnly = true)
	public List<GroupRoleQueried> getOtherGroupRoles(Long id, String service) {
		List<RoleInfo> others = groupRoleService.getOtherGroupRoles(id, service);
		return assembler.transformList(others);
	}

	/**
	 * 透過 Group 與服務查詢群組角色資料
	 * 
	 * @param id      Group id
	 * @param service Service
	 * @return List<GroupRoleQueried>
	 */
	@Transactional(readOnly = true)
	public List<GroupRoleQueried> getGroupRoles(Long id, String service) {
		List<RoleInfo> groupRoles = groupRoleService.getGroupRoles(id, service);
		return assembler.transformList(groupRoles);
	}

}
