package com.example.demo.iface.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleOptionQueriedResource {

	private Long id;

	private String code; // 角色 Code

	private String name;

}
