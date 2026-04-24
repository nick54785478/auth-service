package com.example.demo.iface.dto.in;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintainPermissionQueriedResource {

	private List<String> permissionList;
}
