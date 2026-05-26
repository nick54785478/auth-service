package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.assembler.FunctionAssembler;
import com.example.demo.application.shared.dto.FunctionInfoQueried;
import com.example.demo.application.shared.dto.FunctionOptionQueried;
import com.example.demo.application.shared.query.GetFunctionsSummaryQuery;
import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.function.repository.FunctionInfoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FunctionQueryService {

	private FunctionAssembler assembler;
	private FunctionInfoRepository functionInfoRepository;

	/**
	 * 查詢符合條件的功能資料
	 * 
	 * @param query {@link GetFunctionsSummaryQuery}
	 * @return List<GroupInfoQueried>
	 */
	@Transactional(readOnly = true)
	public List<FunctionInfoQueried> summary(GetFunctionsSummaryQuery query) {
		List<FunctionInfo> functions = functionInfoRepository.summary(query.getService(), query.getType(),
				query.getActionType(), query.getName(), query.getActiveFlag());
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
		List<FunctionInfo> functions = functionInfoRepository.findByServiceAndKeyword(service, keyword);
		return assembler.transformFunctionOptions(functions);
	}
}
