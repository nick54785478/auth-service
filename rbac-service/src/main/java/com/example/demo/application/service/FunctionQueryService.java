package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.assembler.FunctionAssembler;
import com.example.demo.application.shared.dto.FunctionInfoQueried;
import com.example.demo.application.shared.dto.FunctionOptionQueried;
import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.infra.persistence.FunctionInfoPersistence;
import com.example.demo.infra.spec.GetFunctionsOptionsSpecification;
import com.example.demo.infra.spec.GetFunctionsSummarySpecification;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FunctionQueryService {

	private FunctionAssembler assembler;
	private FunctionInfoPersistence functionInfoRepository;

	/**
	 * 查詢符合條件的功能資料
	 * 
	 * @param service    服務
	 * @param actionType 動作種類
	 * @param type       種類
	 * @param name       名稱
	 * @param activeFlag 是否生效
	 * @return List<GroupInfoQueried>
	 */
	@Transactional(readOnly = true)
	public List<FunctionInfoQueried> summary(String service, String actionType, String type, String name,
			String activeFlag) {
		GetFunctionsSummarySpecification specification = new GetFunctionsSummarySpecification(service, type, actionType,
				name, activeFlag);
		List<FunctionInfo> functions = functionInfoRepository.findAll(specification.toSpecification());
		return assembler.transformFunctions(functions);
	}

	/**
	 * 模糊查詢符合條件的群組資料
	 * 
	 * @param service 服務
	 * @param keyword 關鍵字
	 * @return List<FunctionInfoQueried>
	 */
	public List<FunctionOptionQueried> query(String service, String keyword) {
		GetFunctionsOptionsSpecification specification = new GetFunctionsOptionsSpecification(service, keyword);
		List<FunctionInfo> functions = functionInfoRepository.findAll(specification.toSpecification());
		return assembler.transformFunctionOptions(functions);
	}
}
