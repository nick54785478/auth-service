package com.example.demo.application.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleOptionQueried {

	private Long id;

	private String code; // 角色 Code

	private String name;

}
