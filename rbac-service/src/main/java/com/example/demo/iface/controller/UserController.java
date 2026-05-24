package com.example.demo.iface.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.service.UserCommandService;
import com.example.demo.application.service.UserQueryService;
import com.example.demo.application.shared.command.CreateUserCommand;
import com.example.demo.application.shared.command.UpdateUserCommand;
import com.example.demo.application.shared.dto.UserDetailsQueried;
import com.example.demo.application.shared.dto.UserInfoQueried;
import com.example.demo.iface.dto.request.CreateUserResource;
import com.example.demo.iface.dto.request.UpdateUserResource;
import com.example.demo.iface.dto.response.response.UserCreatedResource;
import com.example.demo.iface.dto.response.response.UserDetailsGottenResource;
import com.example.demo.iface.dto.response.response.UserInfoGottenResource;
import com.example.demo.iface.dto.response.response.UserUpdatedResource;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

	private UserQueryService userQueryService;
	private UserCommandService userCommandService;

	/**
	 * 註冊 使用者資料
	 * 
	 * @param resource
	 * @return ResponseEntity<UserCreatedResource>
	 */
	@PostMapping("/register")
	public ResponseEntity<UserCreatedResource> create(@RequestBody CreateUserResource resource) {
		// 防腐處理 resource -> command
		CreateUserCommand command = BaseDataTransformer.transformData(resource, CreateUserCommand.class);
		userCommandService.create(command);
		return new ResponseEntity<>(new UserCreatedResource("201", "註冊成功"), HttpStatus.CREATED);
	}

	/**
	 * 更新 使用者資料
	 * 
	 * @param resource
	 * @return ResponseEntity<UserUpdatedResource>
	 */
	@PutMapping("/{id}")
	public ResponseEntity<UserUpdatedResource> update(@RequestBody UpdateUserResource resource, @PathVariable Long id) {
		// 防腐處理 resource -> command
		UpdateUserCommand command = BaseDataTransformer.transformData(resource, UpdateUserCommand.class);
		userCommandService.update(id, command);
		return new ResponseEntity<>(new UserUpdatedResource("200", "更新使用者資料成功"), HttpStatus.OK);
	}

//	/**
//	 * 查詢該使用者相關群組資訊
//	 * 
//	 * @param username
//	 * @return ResponseEntity<List<UserGroupQueriedResource>>
//	 */
//	@GetMapping("/{username}/groups")
//	public ResponseEntity<List<UserGroupDetailsQueriedResource>> queryGroups(@PathVariable String username) {
//		List<UserGroupQueried> userGroups = userQueryService.queryGroups(username);
//		return new ResponseEntity<>(
//				BaseDataTransformer.transformData(userGroups, UserGroupDetailsQueriedResource.class), HttpStatus.OK);
//	}
//
//	/**
//	 * 查詢該使用者相關角色資訊
//	 * 
//	 * @param username
//	 * @return ResponseEntity<List<UserRoleQueriedResource>>
//	 */
//	@GetMapping("/{username}/roles")
//	public ResponseEntity<List<UserRoleQueriedResource>> queryRoles(@PathVariable String username) {
//		List<UserRoleQueried> userRoles = userQueryService.queryRoles(username);
//		return new ResponseEntity<>(BaseDataTransformer.transformData(userRoles, UserRoleQueriedResource.class),
//				HttpStatus.OK);
//	}

	/**
	 * 查詢該使用者相關資訊(含權限、角色)
	 * 
	 * @param username 使用者名稱
	 * @param service  服務
	 * @return ResponseEntity<UserDetailsGottenResource>
	 */
	@GetMapping("/{username}/details")
	public ResponseEntity<UserDetailsGottenResource> queryUserDetails(@PathVariable String username,
			@RequestParam(defaultValue = "AUTH_SERVICE") String service) {
		UserDetailsQueried data = userQueryService.getUserDetails(username, service);
		return new ResponseEntity<>(new UserDetailsGottenResource("200", "查詢成功", data), HttpStatus.OK);
	}

	/**
	 * 查詢該使用者資料
	 * 
	 * @param username
	 * @return ResponseEntity<UserInfoGottenResource>
	 */
	@GetMapping("/{username}")
	public ResponseEntity<UserInfoGottenResource> query(@PathVariable String username) {
		UserInfoQueried data = userQueryService.query(username);
		return new ResponseEntity<>(new UserInfoGottenResource("200", "查詢成功", data), HttpStatus.OK);
	}

}
