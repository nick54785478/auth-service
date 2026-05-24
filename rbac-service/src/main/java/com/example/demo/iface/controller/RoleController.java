package com.example.demo.iface.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.service.RoleCommandService;
import com.example.demo.application.service.RoleQueryService;
import com.example.demo.application.shared.command.CreateOrUpdateRoleCommand;
import com.example.demo.application.shared.command.CreateRoleCommand;
import com.example.demo.application.shared.command.UpdateRoleCommand;
import com.example.demo.application.shared.dto.RoleInfoQueried;
import com.example.demo.iface.dto.request.CreateOrUpdateRoleResource;
import com.example.demo.iface.dto.request.CreateRoleResource;
import com.example.demo.iface.dto.request.UpdateRoleResource;
import com.example.demo.iface.dto.response.response.RoleCreatedOrUpdatedResource;
import com.example.demo.iface.dto.response.response.RoleCreatedResource;
import com.example.demo.iface.dto.response.response.RoleDeletedResource;
import com.example.demo.iface.dto.response.response.RoleUpdatedResource;
import com.example.demo.iface.dto.response.response.RolesSummaryGottenResource;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController {

	private RoleQueryService roleQueryService;
	private RoleCommandService roleCommandService;

	/**
	 * 新增 角色資料
	 * 
	 * @param resource
	 * @return ResponseEntity<RoleCreatedResource>
	 */
	@PostMapping("")
	public ResponseEntity<RoleCreatedResource> create(@RequestBody CreateRoleResource resource) {
		// 防腐處理 resource -> command
		CreateRoleCommand command = BaseDataTransformer.transformData(resource, CreateRoleCommand.class);
		roleCommandService.create(command);
		return new ResponseEntity<>(new RoleCreatedResource("201", "成功新增一筆角色資料"), HttpStatus.CREATED);
	}

	/**
	 * 新增/更新多筆角色資料
	 * 
	 * @param resource
	 * @return ResponseEntity<RoleCreatedResource>
	 */
	@PostMapping("/saveList")
	public ResponseEntity<RoleCreatedOrUpdatedResource> save(@RequestBody List<CreateOrUpdateRoleResource> resources) {
		// 防腐處理 resource -> command
		List<CreateOrUpdateRoleCommand> commands = BaseDataTransformer.transformData(resources,
				CreateOrUpdateRoleCommand.class);
		roleCommandService.createOrUpdate(commands);
		return new ResponseEntity<>(new RoleCreatedOrUpdatedResource("200", "新增/更新多筆角色資料"), HttpStatus.OK);
	}

	/**
	 * 更新 角色資料
	 * 
	 * @param resource
	 * @return ResponseEntity<RoleCreatedResource>
	 */
	@PutMapping("/{id}")
	public ResponseEntity<RoleUpdatedResource> update(@RequestBody UpdateRoleResource resource, @PathVariable Long id) {
		// 防腐處理 resource -> command
		UpdateRoleCommand command = BaseDataTransformer.transformData(resource, UpdateRoleCommand.class);
		command.setId(id);
		roleCommandService.update(command);
		return new ResponseEntity<>(new RoleUpdatedResource("200", "更新角色成功"), HttpStatus.OK);
	}

	/**
	 * 刪除多筆角色資料
	 * 
	 * @param ids 要被刪除的 id 清單
	 * @return ResponseEntity<RoleDeletedResource>
	 */
	@DeleteMapping("")
	public ResponseEntity<RoleDeletedResource> delete(@RequestBody List<Long> ids) {
		roleCommandService.delete(ids);
		return new ResponseEntity<>(new RoleDeletedResource("200", "成功刪除多筆角色資料"), HttpStatus.OK);
	}


	/**
	 * 查詢角色資料
	 * 
	 * @param type
	 * @param name
	 * @return ResponseEntity<List<RoleQueriedResource>>
	 */
	@GetMapping("/summary")
	public ResponseEntity<RolesSummaryGottenResource> summary(@RequestParam String service,
			@RequestParam(required = false) String type, @RequestParam(required = false) String name,
			@RequestParam(required = false) String activeFlag) {
		List<RoleInfoQueried> data = roleQueryService.summary(service, type, name, activeFlag);
		return new ResponseEntity<>(new RolesSummaryGottenResource("200", "查詢成功", data), HttpStatus.OK);
	}

}
