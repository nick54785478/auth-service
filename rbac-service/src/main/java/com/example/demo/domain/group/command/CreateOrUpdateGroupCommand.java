package com.example.demo.domain.group.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateGroupCommand {

	private Long id;

	private String service;

	private String type;
	
	private String code;
	
	private String name;
	
	private String description;
	
	private String activeFlag;
}
