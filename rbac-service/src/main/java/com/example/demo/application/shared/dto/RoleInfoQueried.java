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
public class RoleInfoQueried {

	private Long id;

	private String service;

	private String code; // 角色 Code

	private String name;

	private String type; // 權限種類

	private String description; // 敘述

	@Default
	private YesNo activeFlag = YesNo.Y; // 是否有效
}
