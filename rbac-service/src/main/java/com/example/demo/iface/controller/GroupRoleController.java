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
import com.example.demo.domain.group.command.UpdateGroupRolesCommand;
import com.example.demo.iface.dto.in.UpdateGroupRolesResource;
import com.example.demo.iface.dto.out.GroupRoleQueriedResource;
import com.example.demo.iface.dto.out.GroupRolesUpdatedResource;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/groups/roles")
public class GroupRoleController {

	private GroupRoleQueryService groupRoleQueryService;
	private GroupRoleCommandService groupRoleCommandService;

	/**
	 * 查詢不屬於該角色的功能資料
	 * 
	 * @param id      群組ID
	 * @param service 服務
	 * @return ResponseEntity<List<GroupRoleQueriedResource>>
	 */
	@GetMapping("/{id}/others")
	public ResponseEntity<List<GroupRoleQueriedResource>> queryOthers(@PathVariable Long id,
			@RequestParam String service) {
		return new ResponseEntity<>(BaseDataTransformer.transformData(groupRoleQueryService.queryOthers(id, service),
				GroupRoleQueriedResource.class), HttpStatus.OK);
	}

	/**
	 * 更新群組內的角色
	 * 
	 * @param resource
	 * @return ResponseEntity<GroupRolesUpdatedResource>
	 */
	@PostMapping("/update")
	public ResponseEntity<GroupRolesUpdatedResource> update(@RequestBody UpdateGroupRolesResource resource) {

		UpdateGroupRolesCommand command = BaseDataTransformer.transformData(resource, UpdateGroupRolesCommand.class);
		groupRoleCommandService.update(command);
		return new ResponseEntity<>(new GroupRolesUpdatedResource("200", "成功更新群組內角色權限"), HttpStatus.OK);
	}
}
