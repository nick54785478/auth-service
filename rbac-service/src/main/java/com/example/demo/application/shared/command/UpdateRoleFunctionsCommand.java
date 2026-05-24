package com.example.demo.application.shared.command;

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
