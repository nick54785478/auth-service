package com.example.demo.iface.dto.out;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidatedAndParsedResource {

	private String username;

	private List<String> roles;  // 角色清單

	private List<String> groups;  // 群組清單

	private List<String> functions; // 功能清單
}
