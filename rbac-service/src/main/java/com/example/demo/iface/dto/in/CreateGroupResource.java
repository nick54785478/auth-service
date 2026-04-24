package com.example.demo.iface.dto.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupResource {
	
	private String service;

	private String name; // 名稱
	
	private String code; // 群組代號

	private String description; // 敘述

}
