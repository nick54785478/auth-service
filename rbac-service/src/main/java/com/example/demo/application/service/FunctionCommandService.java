package com.example.demo.application.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.shared.command.CreateFunctionCommand;
import com.example.demo.application.shared.command.UpsertFunctionCommand;
import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.function.aggregate.vo.FunctionProfile;
import com.example.demo.domain.function.aggregate.vo.FunctionScope;
import com.example.demo.infra.repository.FunctionInfoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class FunctionCommandService {

	private FunctionInfoRepository functionRepository;

	/**
	 * 建立一筆功能資料
	 * 
	 * @param command
	 * @return FunctionCreated
	 */
	public void create(CreateFunctionCommand command) {
		FunctionScope scope = FunctionScope.of(command.getService(), command.getCode());
		FunctionProfile profile = FunctionProfile.of(command.getName(), command.getDescription());
		FunctionInfo function = FunctionInfo.create(scope, profile, command.getType(), command.getActionType());
		functionRepository.save(function);
	}

	/**
	 * 建立多筆功能資料
	 * 
	 * @param commands {@link UpsertFunctionCommand} 清單
	 */
	public void upsert(List<UpsertFunctionCommand> commands) {
		// 取得 id 清單
		List<Long> ids = commands.stream().filter(command -> command.getId() != null).map(UpsertFunctionCommand::getId)
				.collect(Collectors.toList());

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

	/**
	 * 刪除多筆功能資料
	 * 
	 * @param ids 要被刪除的 id 清單
	 */
	public void delete(List<Long> ids) {
		List<FunctionInfo> functions = functionRepository.findByIdIn(ids);
		functions.stream().forEach(FunctionInfo::delete);
		functionRepository.saveAll(functions);
	}

}
