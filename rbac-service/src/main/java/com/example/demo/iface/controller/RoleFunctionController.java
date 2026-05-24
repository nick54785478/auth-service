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

import com.example.demo.application.service.RoleFunctionCommandService;
import com.example.demo.application.service.RoleFunctionQueryService;
import com.example.demo.application.shared.dto.RoleFunctionQueried;
import com.example.demo.domain.shared.command.UpdateRoleFunctionsCommand;
import com.example.demo.iface.dto.request.UpdateRoleFunctionsResource;
import com.example.demo.iface.dto.response.response.RoleFunctionsGottenResource;
import com.example.demo.iface.dto.response.response.RoleFunctionsUpdatedResource;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/roles/functions")
public class RoleFunctionController {

	private RoleFunctionQueryService roleFunctionQueryService;
	private RoleFunctionCommandService roleFunctionCommandService;

	/**
	 * 更新角色的權限
	 * 
	 * @param resource
	 * @return ResponseEntity<RoleFunctionsUpdatedResource>
	 */
	@PostMapping("/update")
	public ResponseEntity<RoleFunctionsUpdatedResource> updateFunctions(
			@RequestBody UpdateRoleFunctionsResource resource) {
		UpdateRoleFunctionsCommand command = BaseDataTransformer.transformData(resource,
				UpdateRoleFunctionsCommand.class);
		roleFunctionCommandService.updateFunctions(command);
		return new ResponseEntity<>(new RoleFunctionsUpdatedResource("200", "成功更新角色功能權限"), HttpStatus.OK);
	}

	/**
	 * 查詢不屬於該角色的功能資料
	 * 
	 * @param id      Role id
	 * @param service Service
	 * @return ResponseEntity<RoleFunctionsGottenResource>
	 */
	@GetMapping("/{id}/others")
	public ResponseEntity<RoleFunctionsGottenResource> getOtherRoleFunctions(@PathVariable Long id,
			@RequestParam String service) {
		List<RoleFunctionQueried> data = roleFunctionQueryService.getOtherRoleFunctions(id, service);
		return new ResponseEntity<>(new RoleFunctionsGottenResource("200", "查詢成功", data), HttpStatus.OK);
	}

	/**
	 * 查詢屬於該角色的功能資料
	 * 
	 * @param id      Role id
	 * @param service Service
	 * @return ResponseEntity<RoleFunctionsGottenResource>
	 */
	@GetMapping("/{id}")
	public ResponseEntity<RoleFunctionsGottenResource> getRoleFunctions(@PathVariable Long id,
			@RequestParam String service) {
		List<RoleFunctionQueried> data = roleFunctionQueryService.getRoleFunctions(id, service);
		return new ResponseEntity<>(new RoleFunctionsGottenResource("200", "查詢成功", data), HttpStatus.OK);
	}
}
