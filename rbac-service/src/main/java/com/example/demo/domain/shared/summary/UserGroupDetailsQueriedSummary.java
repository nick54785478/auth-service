package com.example.demo.domain.shared.summary;

import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupDetailsQueriedSummary {

	private Long id;
	
	private String service;

	private String type;

	private String name; // 名稱

	private String code; // 群組代號

	private String description; // 敘述

	private YesNo activeFlag;
}
