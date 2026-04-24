package com.example.demo.iface.dto.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateRoleResource {

	private Long id;
	
	private String service;

	private String code;
	
	private String name;
	
	private String type;
	
	private String description;
	
	private String activeFlag;
	
}
