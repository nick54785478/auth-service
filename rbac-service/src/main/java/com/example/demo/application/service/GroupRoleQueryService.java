package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.shared.dto.GroupRoleQueried;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.service.GroupRoleService;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GroupRoleQueryService {

	private GroupRoleService groupRoleService;

	/**
	 * 查詢其他(不屬於該群組)的角色
	 * 
	 * @param id      群組ID
	 * @param service 服務
	 * @return
	 */
	@Transactional
	public List<GroupRoleQueried> queryOthers(Long id, String service) {
		List<RoleInfo> others = groupRoleService.queryOthers(id, service);
		return BaseDataTransformer.transformData(others, GroupRoleQueried.class);
	}
}
