package com.example.demo.iface.dto.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleResource {

	private Long id;
	
	private String code;

	private String name;

	private String description;
	
	private String type;

	private String activeFlag; // 是否有效

	public UpdateRoleResource(Long id, String name, String code, String type, String description) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.type = type;
		this.description = description;
	}

}
