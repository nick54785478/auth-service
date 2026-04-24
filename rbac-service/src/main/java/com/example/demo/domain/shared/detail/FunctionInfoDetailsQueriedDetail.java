package com.example.demo.domain.shared.detail;

import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FunctionInfoDetailsQueriedDetail {

	private Long id;
	
	private String service;

	private String type; // 配置種類

	private String code; // Code

	private String name;
	
	private String actionType;

	private String description; // 敘述
	
	private String label; // 權限(群組角色、個人角色)

	private YesNo activeFlag; // 是否有效
}
