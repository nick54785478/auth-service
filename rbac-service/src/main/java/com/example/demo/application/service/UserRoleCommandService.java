package com.example.demo.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.service.UserRoleService;
import com.example.demo.domain.user.command.UpdateUserRolesCommand;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class UserRoleCommandService {

	private UserRoleService userRoleService;

	/**
	 * 將使用者們加入特定群組
	 * 
	 * @param command
	 */
	public void update(UpdateUserRolesCommand command) {
		userRoleService.update(command);
	}
}
