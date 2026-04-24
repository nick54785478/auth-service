package com.example.demo.application.shared.dto;

import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfoQueried {

	private Long id;

	private String service;

	private String type; // 配置種類

	private String code; // Code

	private String name;

	private String description; // 敘述

//	@Default
//	private List<RoleInfoQueried> roles = new ArrayList<>();

	@Default
	private YesNo activeFlag = YesNo.Y; // 是否有效
}
