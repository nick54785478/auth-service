package com.example.demo.iface.dto.out;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupRolesAddedResource {

	private String groupName;
	
	private List<String> roleList;
}
