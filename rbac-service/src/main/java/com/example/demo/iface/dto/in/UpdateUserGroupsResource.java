package com.example.demo.iface.dto.in;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserGroupsResource {

	private String username;	// 使用者帳號
	
	private String service;
	
	private List<Long> groupIds; // 群組 ID 清單
}
