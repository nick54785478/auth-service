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
import com.example.demo.domain.role.command.UpdateRoleFunctionsCommand;
import com.example.demo.iface.dto.in.UpdateRoleFunctionsResource;
import com.example.demo.iface.dto.out.RoleFunctionQueriedResource;
import com.example.demo.iface.dto.out.RoleFunctionsUpdatedResource;
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
	 * @param id      角色ID
	 * @param service 服務
	 * @return ResponseEntity<List<RoleQueriedResource>>
	 */
	@GetMapping("/{id}/others")
	public ResponseEntity<List<RoleFunctionQueriedResource>> query(@PathVariable Long id,
			@RequestParam String service) {
		return new ResponseEntity<>(BaseDataTransformer.transformData(roleFunctionQueryService.queryOthers(id, service),
				RoleFunctionQueriedResource.class), HttpStatus.OK);
	}

}
