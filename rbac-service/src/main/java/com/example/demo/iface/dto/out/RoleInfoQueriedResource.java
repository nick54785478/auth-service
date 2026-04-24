package com.example.demo.iface.dto.out;

import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleInfoQueriedResource {

	private Long id;
	
	private String service;

	private String code; // 角色 Code

	private String name;

	private String type; // 權限種類

	private String description; // 敘述

	private YesNo activeFlag = YesNo.Y; // 是否有效
}
