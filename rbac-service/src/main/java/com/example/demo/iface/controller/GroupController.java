package com.example.demo.iface.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.service.GroupCommandService;
import com.example.demo.application.service.GroupQueryService;
import com.example.demo.domain.group.command.CreateGroupCommand;
import com.example.demo.domain.group.command.CreateOrUpdateGroupCommand;
import com.example.demo.iface.dto.in.CreateGroupResource;
import com.example.demo.iface.dto.in.CreateOrUpdateGroupResource;
import com.example.demo.iface.dto.out.GroupCreatedOrUpdatedResource;
import com.example.demo.iface.dto.out.GroupCreatedResource;
import com.example.demo.iface.dto.out.GroupDeletedResource;
import com.example.demo.iface.dto.out.GroupInfoQueriedResource;
import com.example.demo.iface.dto.out.GroupRolesQueriedResource;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/groups")
public class GroupController {

	private GroupQueryService groupQueryService;
	private GroupCommandService groupCommandService;

	/**
	 * 新增 群組資料
	 * 
	 * @param resource
	 * @return ResponseEntity<GroupCreatedResource>
	 */
	@PostMapping("")
	public ResponseEntity<GroupCreatedResource> create(@RequestBody CreateGroupResource resource) {
		// 防腐處理 resource -> command
		CreateGroupCommand command = BaseDataTransformer.transformData(resource, CreateGroupCommand.class);
		groupCommandService.create(command);
		return new ResponseEntity<>(new GroupCreatedResource("200", "成功新增一筆群組資料"), HttpStatus.OK);
	}

	/**
	 * 新增/更新多筆群組資料
	 * 
	 * @param resource
	 * @return ResponseEntity<GroupCreatedOrUpdatedResource>
	 */
	@PostMapping("/saveList")
	public ResponseEntity<GroupCreatedOrUpdatedResource> create(
			@RequestBody List<CreateOrUpdateGroupResource> resources) {
		// 防腐處理 resource -> command
		List<CreateOrUpdateGroupCommand> commands = BaseDataTransformer.transformData(resources,
				CreateOrUpdateGroupCommand.class);
		groupCommandService.createOrUpdate(commands);
		return new ResponseEntity<>(new GroupCreatedOrUpdatedResource("200", "新增/更新多筆角色資料"), HttpStatus.OK);
	}

	/**
	 * 查詢群組資料
	 * 
	 * @param service    服務
	 * @param type       群組種類
	 * @param name       群組名稱
	 * @param activeFlag 是否生效
	 * @return ResponseEntity<List<GroupInfoQueriedResource>>
	 */
	@GetMapping("/query")
	public ResponseEntity<List<GroupInfoQueriedResource>> query(@RequestParam String service,
			@RequestParam(required = false) String type, @RequestParam(required = false) String name,
			@RequestParam(required = false) String activeFlag) {
		return new ResponseEntity<>(
				BaseDataTransformer.transformData(groupQueryService.query(service, type, name, activeFlag),
						GroupInfoQueriedResource.class),
				HttpStatus.OK);
	}

	/**
	 * 透過 ID 查詢角色資料
	 * 
	 * @param type
	 * @param name
	 * @return ResponseEntity<GroupInfoQueriedResource>
	 */
	@GetMapping("/{id}")
	public ResponseEntity<GroupRolesQueriedResource> getGroupInfo(@PathVariable Long id, @RequestParam String service) {
		return new ResponseEntity<>(BaseDataTransformer.transformData(groupQueryService.getGroupInfo(id, service),
				GroupRolesQueriedResource.class), HttpStatus.OK);
	}

	/**
	 * 刪除多筆群組資料
	 * 
	 * @param ids 要被刪除的 id 清單
	 * @return ResponseEntity<GroupDeletedResource>
	 */
	@DeleteMapping("")
	public ResponseEntity<GroupDeletedResource> delete(@RequestBody List<Long> ids) {
		groupCommandService.delete(ids);
		return new ResponseEntity<>(new GroupDeletedResource("200", "成功刪除多筆群組資料"), HttpStatus.OK);
	}

}
