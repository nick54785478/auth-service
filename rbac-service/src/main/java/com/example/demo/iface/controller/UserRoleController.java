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

import com.example.demo.application.service.UserRoleCommandService;
import com.example.demo.application.service.UserRoleQueryService;
import com.example.demo.application.shared.dto.UserRoleQueried;
import com.example.demo.domain.user.command.UpdateUserRolesCommand;
import com.example.demo.iface.dto.request.UpdateUserRolesResource;
import com.example.demo.iface.dto.response.response.UserRolesGottenResource;
import com.example.demo.iface.dto.response.response.UserRolesUpdatedResource;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users/roles")
public class UserRoleController {

	private UserRoleQueryService userRoleQueryService;
	private UserRoleCommandService userRoleCommandService;

	/**
	 * 更新使用者角色權限
	 * 
	 * @param resource
	 * @return ResponseEntity<UserRolesUpdatedResource>
	 */
	@PostMapping("/update")
	public ResponseEntity<UserRolesUpdatedResource> grant(@RequestBody UpdateUserRolesResource resource) {
		// 防腐處理 resource -> command
		UpdateUserRolesCommand command = BaseDataTransformer.transformData(resource, UpdateUserRolesCommand.class);
		userRoleCommandService.update(command);
		return new ResponseEntity<>(new UserRolesUpdatedResource("200", "成功更新使用者角色權限"), HttpStatus.OK);
	}

	/**
	 * 查詢該使用者相關角色資訊
	 * 
	 * @param username
	 * @return ResponseEntity<UserRolesGottenResource>
	 */
	@GetMapping("/{username}")
	public ResponseEntity<UserRolesGottenResource> queryRoles(@PathVariable String username,
			@RequestParam String service) {
		List<UserRoleQueried> data = userRoleQueryService.getUserRoles(username, service);
		return new ResponseEntity<>(new UserRolesGottenResource("200", "查詢成功", data), HttpStatus.OK);
	}

	/**
	 * 查詢不屬於該使用者的群組資料
	 * 
	 * @param username 使用者帳號
	 * @param service  服務
	 * @return ResponseEntity<UserRolesGottenResource>
	 */
	@GetMapping("/{username}/others")
	public ResponseEntity<UserRolesGottenResource> queryOthers(@PathVariable String username,
			@RequestParam String service) {
		List<UserRoleQueried> data = userRoleQueryService.getOtherRoles(username, service);
		return new ResponseEntity<>(new UserRolesGottenResource("200", "查詢成功", data), HttpStatus.OK);
	}

}
