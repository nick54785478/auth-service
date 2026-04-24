package com.example.demo.domain.shared.summary;

import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDetailsQueriedSummary {

	private Long id;
	
	private String service;
	
	private String code;

	private String name;

	private String type; // 權限種類

	private String description; // 敘述

	private YesNo activeFlag;
}
