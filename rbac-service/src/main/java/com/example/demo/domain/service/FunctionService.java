package com.example.demo.domain.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.function.command.CreateOrUpdateFunctionCommand;
import com.example.demo.infra.repository.FunctionInfoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FunctionService {

	private FunctionInfoRepository functionRepository;

	/**
	 * 建立多筆功能資訊(僅限於前端使用 Inline-Edit)
	 * 
	 * @param command
	 */
	public void createOrUpdate(List<CreateOrUpdateFunctionCommand> commands) {
		// 取得 id 清單
		List<Long> ids = commands.stream().filter(command -> command.getId() != null)
				.map(CreateOrUpdateFunctionCommand::getId).collect(Collectors.toList());

		// 取出清單相對應資料
		List<FunctionInfo> functions = functionRepository.findByIdIn(ids);

		Map<Long, FunctionInfo> map = functions.stream()
				.collect(Collectors.toMap(FunctionInfo::getId, Function.identity()));
		List<FunctionInfo> functionList = commands.stream().map(command -> {
			// 修改
			if (!Objects.isNull(command.getId()) && !Objects.isNull(map.get(command.getId()))) {
				FunctionInfo function = map.get(command.getId());
				function.update(command);
				return function;
			} else {
				// 新增
				FunctionInfo function = new FunctionInfo();
				function.create(command);
				return function;
			}
		}).collect(Collectors.toList());
		functionRepository.saveAll(functionList);
	}


}
