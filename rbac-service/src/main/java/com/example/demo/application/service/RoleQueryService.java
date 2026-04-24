package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.shared.dto.RoleInfoQueried;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.service.RoleService;
import com.example.demo.domain.shared.summary.RoleInfoQueriedSummary;
import com.example.demo.infra.repository.RoleInfoRepository;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleQueryService {

	private RoleService roleService;
	private RoleInfoRepository roleInfoRepository;

	/**
	 * 查詢符合條件的角色資料
	 * 
	 * @param service    服務
	 * @param type       角色種類
	 * @param name       角色名稱
	 * @param activeFlag 是否生效
	 * @return List<RoleInfoQueried>
	 */
	@Transactional
	public List<RoleInfoQueried> query(String service, String type, String name, String activeFlag) {
		List<RoleInfo> roles = roleInfoRepository.findAllWithSpecification(service, type, name, activeFlag);
		return BaseDataTransformer.transformData(roles, RoleInfoQueried.class);
	}

	/**
	 * 透過 ID 查詢角色資料
	 * 
	 * @param id      角色ID
	 * @param service 服務
	 * @return RoleInfoQueried
	 */
	@Transactional
	public RoleInfoQueriedSummary getRoleInfo(Long id, String service) {
		return roleService.getRoleInfo(id, service);
	}
}
