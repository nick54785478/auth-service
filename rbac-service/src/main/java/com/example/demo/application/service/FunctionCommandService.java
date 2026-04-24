package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.function.command.CreateFunctionCommand;
import com.example.demo.domain.function.command.CreateOrUpdateFunctionCommand;
import com.example.demo.domain.service.FunctionService;
import com.example.demo.infra.repository.FunctionInfoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class FunctionCommandService {

	private FunctionService functionService;
	private FunctionInfoRepository functionRepository;

	/**
	 * 建立一筆功能資料
	 * 
	 * @param command
	 * @return FunctionCreated
	 */
	public void create(CreateFunctionCommand command) {
		FunctionInfo function = new FunctionInfo();
		function.create(command);
		functionRepository.save(function);
	}

	/**
	 * 建立多筆功能資料
	 * 
	 * @param commands
	 */
	public void createOrUpdate(List<CreateOrUpdateFunctionCommand> commands) {
		functionService.createOrUpdate(commands);
	}

	/**
	 * 刪除多筆功能資料
	 * 
	 * @param ids 要被刪除的 id 清單
	 */
	public void delete(List<Long> ids) {
		List<FunctionInfo> functions = functionRepository.findByIdIn(ids);
		functions.stream().forEach(function -> {
			function.delete();
		});
		functionRepository.saveAll(functions);
	}

}
