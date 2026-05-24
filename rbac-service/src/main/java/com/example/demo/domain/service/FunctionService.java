package com.example.demo.domain.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.function.aggregate.vo.FunctionProfile;
import com.example.demo.domain.function.aggregate.vo.FunctionScope;
import com.example.demo.domain.function.command.UpsertFunctionCommand;
import com.example.demo.infra.repository.FunctionInfoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FunctionService {

	private FunctionInfoRepository functionRepository;

	/**
	 * 建立多筆功能資訊(僅限於前端使用 Inline-Edit)
	 * 
	 * @param command {@link UpsertFunctionCommand} 清單
	 */
	public void createOrUpdate(List<UpsertFunctionCommand> commands) {
		// 取得 id 清單
		List<Long> ids = commands.stream().filter(command -> command.getId() != null)
				.map(UpsertFunctionCommand::getId).collect(Collectors.toList());

		// 取出清單相對應資料
		List<FunctionInfo> functions = functionRepository.findByIdIn(ids);

		Map<Long, FunctionInfo> map = functions.stream()
				.collect(Collectors.toMap(FunctionInfo::getId, Function.identity()));
		List<FunctionInfo> functionList = commands.stream().map(command -> {
			FunctionScope scope = FunctionScope.of(command.getService(), command.getCode());
			FunctionProfile profile = FunctionProfile.of(command.getName(), command.getDescription());

			// 修改
			if (!Objects.isNull(command.getId()) && !Objects.isNull(map.get(command.getId()))) {
				FunctionInfo function = map.get(command.getId());
				function.update(scope, profile, command.getType(), command.getActionType(), command.getActiveFlag());
				return function;
			} else {
				// 新增
				return FunctionInfo.create(scope, profile, command.getType(), command.getActionType());
			}
		}).collect(Collectors.toList());
		functionRepository.saveAll(functionList);
	}

}
