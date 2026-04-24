package com.example.demo.domain.user.command;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRolesCommand {

	private String username;
	
	private String service;

	private List<Long> roleIds;
}
