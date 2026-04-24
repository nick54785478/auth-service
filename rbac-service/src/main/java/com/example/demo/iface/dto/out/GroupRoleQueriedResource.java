package com.example.demo.iface.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupRoleQueriedResource {

	private Long id; // 角色 ID

	private String code; // 角色代號

	private String name; // 名稱

	private String description; // 敘述
}
