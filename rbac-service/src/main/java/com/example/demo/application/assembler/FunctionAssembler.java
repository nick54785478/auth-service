package com.example.demo.application.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.application.shared.dto.FunctionInfoQueried;
import com.example.demo.application.shared.dto.FunctionOptionQueried;
import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.function.aggregate.vo.FunctionProfile;
import com.example.demo.domain.function.aggregate.vo.FunctionScope;

/**
 * 功能資料轉換器 (Function Assembler)
 */
@Component
public class FunctionAssembler {

	/**
	 * 轉換 Function 資料
	 * 
	 * @param function {@link FunctionInfo} 資料
	 * @return {@link FunctionInfoQueried}
	 */
	public FunctionInfoQueried transformFunction(FunctionInfo function) {
		if (function == null) {
			return null;
		}
		FunctionScope scope = function.getScope();
		FunctionProfile profile = function.getProfile();
		return new FunctionInfoQueried(function.getId(), scope.getService(), function.getType(), scope.getCode(),
				profile.getName(), function.getActionType().getCode(), profile.getDescription(),
				function.getActiveFlag());
	}

	/**
	 * 轉換 Function 資料清單
	 * 
	 * @param function {@link FunctionInfo} 清單
	 * @return {@link FunctionInfoQueried} 清單
	 */
	public List<FunctionInfoQueried> transformFunctions(List<FunctionInfo> functions) {
		return functions.stream().map(this::transformFunction).collect(Collectors.toList());
	}

	/**
	 * 轉換 Group Option 資料
	 * 
	 * @param function {@link FunctionInfo} 資料
	 * @return {@link FunctionOptionQueried}
	 */
	public FunctionOptionQueried transformFunctionOption(FunctionInfo function) {
		if (function == null) {
			return null;
		}
		FunctionScope scope = function.getScope();
		FunctionProfile profile = function.getProfile();
		return new FunctionOptionQueried(function.getId(), scope.getCode(), profile.getName());
	}

	/**
	 * 轉換 Function Option 資料清單
	 * 
	 * @param function {@link FunctionInfo} 清單
	 * @return {@link FunctionOptionQueried} 清單
	 */
	public List<FunctionOptionQueried> transformFunctionOptions(List<FunctionInfo> functions) {
		return functions.stream().map(this::transformFunctionOption).collect(Collectors.toList());
	}
}
