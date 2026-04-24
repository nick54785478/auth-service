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

import com.example.demo.application.service.UserGroupCommandService;
import com.example.demo.application.service.UserGroupQueryService;
import com.example.demo.application.shared.dto.UserGroupQueried;
import com.example.demo.domain.user.command.UpdateUserGroupsCommand;
import com.example.demo.iface.dto.in.UpdateUserGroupsResource;
import com.example.demo.iface.dto.out.UserGroupDetailsQueriedResource;
import com.example.demo.iface.dto.out.UserGroupQueriedResource;
import com.example.demo.iface.dto.out.UserGroupUpdatedResource;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users/groups")
public class UserGroupController {

	private UserGroupQueryService userGroupQueryService;
	private UserGroupCommandService userGroupCommandService;

	/**
	 * 將使用者加入特定群組
	 * 
	 * @param resource
	 * @return ResponseEntity<UserGroupUpdatedResource>
	 */
	@PostMapping("/update")
	public ResponseEntity<UserGroupUpdatedResource> update(@RequestBody UpdateUserGroupsResource resource) {
		// 防腐處理 resource -> command
		UpdateUserGroupsCommand command = BaseDataTransformer.transformData(resource, UpdateUserGroupsCommand.class);
		userGroupCommandService.update(command);
		return new ResponseEntity<>(new UserGroupUpdatedResource("200", "成功更新使用者群組權限"), HttpStatus.OK);
	}

	/**
	 * 查詢該使用者相關角色資訊
	 * 
	 * @param username 使用者帳號
	 * @param service  服務
	 * @return ResponseEntity<List<UserGroupQueriedResource>>
	 */
	@GetMapping("/{username}")
	public ResponseEntity<List<UserGroupQueriedResource>> queryRoles(@PathVariable String username,
			@RequestParam String service) {
		List<UserGroupQueried> userGroups = userGroupQueryService.queryGroups(username, service);
		return new ResponseEntity<>(BaseDataTransformer.transformData(userGroups, UserGroupQueriedResource.class),
				HttpStatus.OK);
	}

	/**
	 * 查詢不屬於該使用者的群組資料
	 * 
	 * @param username 使用者帳號
	 * @param service  服務
	 * @return ResponseEntity<List<UserGroupQueriedResource>>
	 */
	@GetMapping("/{usernmae}/others")
	public ResponseEntity<List<UserGroupDetailsQueriedResource>> query(@PathVariable String usernmae,
			@RequestParam String service) {
		return new ResponseEntity<>(
				BaseDataTransformer.transformData(userGroupQueryService.queryOthers(usernmae, service),
						UserGroupDetailsQueriedResource.class),
				HttpStatus.OK);
	}

}
