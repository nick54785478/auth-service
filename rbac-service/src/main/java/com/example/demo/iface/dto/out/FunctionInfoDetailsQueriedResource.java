package com.example.demo.iface.dto.out;

import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionInfoDetailsQueriedResource {

	private Long id;
	
	private String service;

	private String type; // 配置種類

	private String code; // Code

	private String name;
	
	private String actionType;

	private String description; // 敘述
	
	private String label; // 權限(群組角色、個人角色)

	private YesNo activeFlag = YesNo.Y; // 是否有效
}
