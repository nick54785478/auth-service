package com.example.demo.application.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.application.shared.dto.RoleFunctionQueried;
import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.function.aggregate.vo.FunctionProfile;
import com.example.demo.domain.function.aggregate.vo.FunctionScope;

@Component
public class RoleFunctionAssembler {

	/**
	 * 將領域實體 (FunctionInfo) 轉換為角色功能查詢結果 DTO
	 * 
	 * @param function 功能資料 (Aggregate Root)
	 * @return {@link RoleFunctionQueried} 轉換後的 DTO；若傳入 null 則回傳 null
	 */
	public RoleFunctionQueried transform(FunctionInfo function) {
		if (function == null) {
			return null;
		}

		// 1. 提取 Value Objects
		FunctionScope scope = function.getScope();
		FunctionProfile profile = function.getProfile();

		// 2. 防禦性取值，避免資料庫歷史髒資料導致 NullPointerException
		String code = (scope != null) ? scope.getCode() : null;
		String name = (profile != null) ? profile.getName() : null;
		String description = (profile != null) ? profile.getDescription() : null;

		// 3. 安全轉換 Enum (ActionType) 為 Label 字串
		String actionTypeLabel = (function.getActionType() != null) ? function.getActionType().getLabel() : null;

		// 4. 使用 Builder 組裝 DTO
		return RoleFunctionQueried.builder().id(function.getId()).code(code).type(function.getType()).name(name)
				.actionType(actionTypeLabel).description(description).activeFlag(function.getActiveFlag()).build();
	}

	/**
	 * 將領域實體清單 (List<FunctionInfo>) 轉換為角色功能查詢結果清單
	 * 
	 * @param functions 功能資料 (Aggregate Root) 清單
	 * @return {@link RoleFunctionQueried} 清單；若傳入空值或空集合則回傳空 List
	 */
	public List<RoleFunctionQueried> transformList(List<FunctionInfo> functions) {
		if (functions == null || functions.isEmpty()) {
			return List.of(); // 回傳不可變的空集合，保護呼叫端免受 null 困擾
		}

		return functions.stream().map(this::transform).collect(Collectors.toList());
	}
}