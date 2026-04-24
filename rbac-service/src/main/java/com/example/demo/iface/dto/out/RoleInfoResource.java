package com.example.demo.iface.dto.out;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.domain.role.aggregate.entity.RoleFunction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleInfoResource {

	private Long id;
	
	private String code;

	private String name;
	
	private String type;

	private String description; // 敘述
	
	private List<RoleFunction> functions = new ArrayList<>(); // 角色所屬功能

	private String activeFlag; // 是否有效

}
