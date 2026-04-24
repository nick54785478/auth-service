package com.example.demo.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.group.command.UpdateGroupRolesCommand;
import com.example.demo.domain.service.GroupRoleService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class GroupRoleCommandService {

	private GroupRoleService groupRoleService;

	/**
	 * 將角色加入特定群組
	 * 
	 * @param command
	 */
	public void update(UpdateGroupRolesCommand command) {
		groupRoleService.update(command);
	}
}
