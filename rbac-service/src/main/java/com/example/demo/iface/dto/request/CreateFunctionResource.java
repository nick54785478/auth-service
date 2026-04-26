package com.example.demo.iface.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFunctionResource {
	
	private String service;

	private String type; // 種類
	
	private String name;
	
	private String actionType;
	
	private String code; // Code

	private String description; // 敘述
}
