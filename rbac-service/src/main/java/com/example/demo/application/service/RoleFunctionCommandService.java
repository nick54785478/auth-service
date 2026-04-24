package com.example.demo.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.role.command.UpdateRoleFunctionsCommand;
import com.example.demo.domain.service.RoleFunctionService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class RoleFunctionCommandService {

	private RoleFunctionService roleFunctionService;

	/**
	 * 更新角色功能
	 * 
	 * @param command
	 * @return RoleFunctionsUpdated
	 */
	public void updateFunctions(UpdateRoleFunctionsCommand command) {
		roleFunctionService.update(command);
	}
}
