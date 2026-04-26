package com.example.demo.iface.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.service.GroupRoleCommandService;
import com.example.demo.application.service.GroupRoleQueryService;
import com.example.demo.application.shared.dto.GroupRoleQueried;
import com.example.demo.domain.group.command.UpdateGroupRolesCommand;
import com.example.demo.iface.dto.request.UpdateGroupRolesResource;
import com.example.demo.iface.dto.response.response.GroupRolesGottenResource;
import com.example.demo.iface.dto.response.response.GroupRolesUpdatedResource;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/groups/roles")
public class GroupRoleController {

	private GroupRoleQueryService groupRoleQueryService;
	private GroupRoleCommandService groupRoleCommandService;

	/**
	 * 查詢不屬於該角色的功能資料(特定 Service)
	 * 
	 * @param id      Group id
	 * @param service Service
	 * @return ResponseEntity<List<GroupRoleQueriedResource>>
	 */
	@GetMapping("/{id}/others")
	public ResponseEntity<GroupRolesGottenResource> getOtherGroupRoles(@PathVariable Long id,
			@RequestParam String service) {
		List<GroupRoleQueried> data = groupRoleQueryService.getOtherGroupRoles(id, service);
		return new ResponseEntity<>(new GroupRolesGottenResource("200", "查詢成功", data), HttpStatus.OK);
	}

	/**
	 * 透過群組 ID 查詢角色資料(特定 Service)
	 * 
	 * @param id      Group id
	 * @param service Service
	 * @return ResponseEntity<GroupInfoQueriedResource>
	 */
	@GetMapping("/{id}")
	public ResponseEntity<GroupRolesGottenResource> getGroupRoles(@PathVariable Long id, @RequestParam String service) {
		List<GroupRoleQueried> data = groupRoleQueryService.getGroupRoles(id, service);
		return new ResponseEntity<>(new GroupRolesGottenResource("200", "查詢成功", data), HttpStatus.OK);
	}

	/**
	 * 更新群組內的角色
	 * 
	 * @param resource {@link UpdateGroupRolesResource}
	 * @return ResponseEntity<GroupRolesUpdatedResource>
	 */
	@PostMapping("/update")
	public ResponseEntity<GroupRolesUpdatedResource> update(@RequestBody UpdateGroupRolesResource resource) {
		UpdateGroupRolesCommand command = BaseDataTransformer.transformData(resource, UpdateGroupRolesCommand.class);
		groupRoleCommandService.update(command);
		return new ResponseEntity<>(new GroupRolesUpdatedResource("200", "成功更新群組內角色權限"), HttpStatus.OK);
	}
}
