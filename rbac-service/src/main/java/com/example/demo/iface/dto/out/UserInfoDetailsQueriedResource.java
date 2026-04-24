package com.example.demo.iface.dto.out;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDetailsQueriedResource {

	private Long id;

	private String name;

	private String email; // 信箱

	private String username; // 帳號

	private String address;
	
	private String birthday;
	
	private String nationalIdNo;
	
	private String activeFlag;

	private List<UserGroupDetailsQueriedResource> groups;

	private List<UserRoleDetailsQueriedResource> roles;
	
	private List<FunctionInfoDetailsQueriedResource> functions;

}
