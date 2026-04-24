package com.example.demo.iface.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.service.CustomisationCommandService;
import com.example.demo.application.service.CustomisationQueryService;
import com.example.demo.domain.customisation.command.UpdateCustomisationCommand;
import com.example.demo.iface.dto.in.CustomisationUpdatedResource;
import com.example.demo.iface.dto.in.UpdateCustomisationResource;
import com.example.demo.iface.dto.out.FieldViewCustomisationQueriedResource;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/customisation")
public class CustomisationController {

	private CustomisationQueryService customisationQueryService;
	private CustomisationCommandService customisationCommandService;

	/**
	 * 更新個人化設定資料
	 * 
	 * @param resource
	 * @return ResponseEntity<GroupCreatedResource>
	 */
	@PostMapping("")
	public ResponseEntity<CustomisationUpdatedResource> updateCustomisation(
			@RequestBody UpdateCustomisationResource resource) {
		// 防腐處理 resource -> command
		UpdateCustomisationCommand command = BaseDataTransformer.transformData(resource,
				UpdateCustomisationCommand.class);
		customisationCommandService.updateCustomisation(command);
		return new ResponseEntity<>(new CustomisationUpdatedResource("200", "成功更新一筆個人化設定資料"), HttpStatus.OK);
	}

	/**
	 * 查詢表格顯示個人化設定資料
	 * 
	 * @param username  使用者帳號
	 * @param component Component 名稱
	 * @return ResponseEntity<List<FunctionInfoQueriedResource>>
	 */
	@GetMapping("/fieldView")
	public ResponseEntity<List<FieldViewCustomisationQueriedResource>> getFieldViewCustomisation(
			@RequestParam String username, @RequestParam String component) {
		return new ResponseEntity<>(BaseDataTransformer.transformData(
				customisationQueryService.getCustomisation(username, component, "FieldView"),
				FieldViewCustomisationQueriedResource.class), HttpStatus.OK);
	}
}
