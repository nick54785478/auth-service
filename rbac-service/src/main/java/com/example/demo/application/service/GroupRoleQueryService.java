package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.shared.dto.GroupRoleQueried;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.service.GroupRoleService;
import com.example.demo.domain.shared.detail.GroupRoleQueriedDetail;
import com.example.demo.domain.shared.summary.GroupInfoQueriedSummary;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class GroupRoleQueryService {

	private GroupRoleService groupRoleService;

	/**
	 * 查詢其他(不屬於該群組)的角色
	 * 
	 * @param id      群組ID
	 * @param service 服務
	 * @return List<GroupRoleQueried>
	 */
	@Transactional(readOnly = true)
	public List<GroupRoleQueried> getOtherGroupRoles(Long id, String service) {
		List<RoleInfo> others = groupRoleService.getOtherGroupRoles(id, service);
		return BaseDataTransformer.transformData(others, GroupRoleQueried.class);
	}

	/**
	 * 透過 Group 與服務查詢群組角色資料
	 * 
	 * @param id      Group id
	 * @param service 服務
	 * @return List<GroupRoleQueried>
	 */
	@Transactional(readOnly = true)
	public List<GroupRoleQueried> getGroupRoles(Long id, String service) {
		List<GroupRoleQueriedDetail> groupRoles = groupRoleService.getGroupRoles(id, service);
		return BaseDataTransformer.transformData(groupRoles, GroupRoleQueried.class);
	}

}
