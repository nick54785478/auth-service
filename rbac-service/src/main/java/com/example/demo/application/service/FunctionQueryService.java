package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.application.shared.dto.FunctionInfoQueried;
import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.infra.repository.FunctionInfoRepository;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FunctionQueryService {

	private FunctionInfoRepository functionInfoRepository;

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
	public List<FunctionInfoQueried> query(String service, String actionType, String type, String name,
			String activeFlag) {
		List<FunctionInfo> functions = functionInfoRepository.findAllWithSpecification(service, actionType, type, name,
				activeFlag);
		return BaseDataTransformer.transformData(functions, FunctionInfoQueried.class);
	}

	/**
	 * 模糊查詢符合條件的群組資料
	 * 
	 * @param queryStr
	 * @return List<GroupInfoQueried>
	 */
	public List<FunctionInfoQueried> query(String queryStr) {
		List<FunctionInfo> functions = functionInfoRepository.findAllWithSpecification(queryStr);
		return BaseDataTransformer.transformData(functions, FunctionInfoQueried.class);
	}
}
