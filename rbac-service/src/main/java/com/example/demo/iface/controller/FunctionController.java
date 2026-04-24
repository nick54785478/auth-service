package com.example.demo.iface.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.service.FunctionCommandService;
import com.example.demo.application.service.FunctionQueryService;
import com.example.demo.domain.function.command.CreateFunctionCommand;
import com.example.demo.domain.function.command.CreateOrUpdateFunctionCommand;
import com.example.demo.iface.dto.in.CreateFunctionResource;
import com.example.demo.iface.dto.in.CreateOrUpdateFunctionResource;
import com.example.demo.iface.dto.out.FunctionCreatedOrUpdatedResource;
import com.example.demo.iface.dto.out.FunctionCreatedResource;
import com.example.demo.iface.dto.out.FunctionDeletedResource;
import com.example.demo.iface.dto.out.FunctionInfoQueriedResource;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/functions")
public class FunctionController {

	private FunctionCommandService functionCommandService;
	private FunctionQueryService functionQueryService;

	/**
	 * 新增 功能資料
	 * 
	 * @param resource
	 * @return ResponseEntity<FunctionCreatedResource>
	 */
	@PostMapping("")
	public ResponseEntity<FunctionCreatedResource> create(@RequestBody CreateFunctionResource resource) {
		// 防腐處理 resource -> command
		CreateFunctionCommand command = BaseDataTransformer.transformData(resource, CreateFunctionCommand.class);
		functionCommandService.create(command);
		return new ResponseEntity<>(new FunctionCreatedResource("200", "新增成功"), HttpStatus.CREATED);
	}

	/**
	 * 新增/更新多筆功能資料
	 * 
	 * @param resource
	 * @return ResponseEntity<FunctionCreatedOrUpdatedResource>
	 */
	@PostMapping("/saveList")
	public ResponseEntity<FunctionCreatedOrUpdatedResource> save(
			@RequestBody List<CreateOrUpdateFunctionResource> resources) {
		// 防腐處理 resource -> command
		List<CreateOrUpdateFunctionCommand> commands = BaseDataTransformer.transformData(resources,
				CreateOrUpdateFunctionCommand.class);
		functionCommandService.createOrUpdate(commands);
		return new ResponseEntity<>(new FunctionCreatedOrUpdatedResource("200", "新增/更新多筆功能資料"), HttpStatus.OK);
	}

	/**
	 * 刪除多筆功能資料
	 * 
	 * @param ids 要被刪除的 id 清單
	 * @return ResponseEntity<FunctionDeletedResource>
	 */
	@DeleteMapping("")
	public ResponseEntity<FunctionDeletedResource> delete(@RequestBody List<Long> ids) {
		functionCommandService.delete(ids);
		return new ResponseEntity<>(new FunctionDeletedResource("200", "成功刪除多筆功能資料"), HttpStatus.OK);
	}

	/**
	 * 查詢角色功能資料
	 * 
	 * @param queryStr
	 * @return ResponseEntity<List<FunctionInfoQueriedResource>>
	 */
	@GetMapping("/queryRoleFunc")
	public ResponseEntity<List<FunctionInfoQueriedResource>> query(@RequestParam String queryStr) {
		return new ResponseEntity<>(BaseDataTransformer.transformData(functionQueryService.query(queryStr),
				FunctionInfoQueriedResource.class), HttpStatus.OK);
	}

	/**
	 * 查詢功能資料
	 * 
	 * @param service
	 * @param actionType
	 * @param type
	 * @param name
	 * @param actionFlag
	 * @return ResponseEntity<List<RoleQueriedResource>>
	 */
	@GetMapping("/query")
	public ResponseEntity<List<FunctionInfoQueriedResource>> query(@RequestParam String service,
			@RequestParam(required = false) String actionType, @RequestParam(required = false) String type,
			@RequestParam(required = false) String name, @RequestParam(required = false) String activeFlag) {
		return new ResponseEntity<>(BaseDataTransformer.transformData(
				functionQueryService.query(service, actionType, type, name, activeFlag),
				FunctionInfoQueriedResource.class), HttpStatus.OK);
	}
}
