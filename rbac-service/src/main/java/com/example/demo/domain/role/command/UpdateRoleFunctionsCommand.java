package com.example.demo.domain.role.command;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleFunctionsCommand {

	private Long roleId;
	
	private List<Long> functions;
}
