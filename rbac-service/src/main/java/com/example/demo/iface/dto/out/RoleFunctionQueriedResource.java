package com.example.demo.iface.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleFunctionQueriedResource {

	private Long id;

	private String type;

	private String code;

	private String name;

	private String actionType;

	private String description;

	private String activeFlag;

}
