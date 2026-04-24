package com.example.demo.iface.dto.out;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupsQueriedResource {

	private Long id;

	private String name;

	private String email; // 信箱

	private String username; // 帳號

	private String address;

	private List<UserGroupDetailsQueriedResource> groups;

}
