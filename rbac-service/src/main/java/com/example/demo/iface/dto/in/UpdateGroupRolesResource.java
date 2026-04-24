package com.example.demo.iface.dto.in;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGroupRolesResource {

	private Long groupId; // 群組 ID

	private List<Long> roleIds; // 角色清單
}
