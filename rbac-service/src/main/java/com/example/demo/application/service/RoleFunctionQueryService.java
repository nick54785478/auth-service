package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.shared.dto.RoleFunctionQueried;
import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.service.RoleFunctionService;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleFunctionQueryService {

	private RoleFunctionService roleFunctionService;

	/**
	 * 查詢其他(不屬於該角色)的功能
	 * 
	 * @param id      角色ID
	 * @param service 服務
	 * @return List<RoleFunctionQueried> 角色功能清單
	 */
	@Transactional(readOnly = true)
	public List<RoleFunctionQueried> getOtherRoleFunctions(Long id, String service) {
		List<FunctionInfo> others = roleFunctionService.queryOthers(id, service);
		return BaseDataTransformer.transformData(others, RoleFunctionQueried.class);
	}
	
	/**
	 * 查詢屬於該角色的功能
	 * 
	 * @param id      角色ID
	 * @param service 服務
	 * @return List<RoleFunctionQueried> 角色功能清單
	 */
	@Transactional(readOnly = true)
	public List<RoleFunctionQueried> getRoleFunctions(Long id, String service) {
		List<FunctionInfo> functions = roleFunctionService.getRoleFunctions(id, service);
		return BaseDataTransformer.transformData(functions, RoleFunctionQueried.class);
	}
}
