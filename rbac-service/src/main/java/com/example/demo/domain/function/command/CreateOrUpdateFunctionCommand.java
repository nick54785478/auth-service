package com.example.demo.domain.function.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateFunctionCommand {

	private Long id;
	
	private String service;

	private String code;
	
	private String name;

	private String type;
	
	private String actionType;
	
	private String description;
	
	private String activeFlag;
	
}
