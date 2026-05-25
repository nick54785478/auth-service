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
import com.example.demo.application.shared.command.UpsertCustomisationCommand;
import com.example.demo.application.shared.dto.FieldViewCustomisationQueried;
import com.example.demo.iface.dto.request.UpsertCustomisationResource;
import com.example.demo.iface.dto.response.response.CustomisationUpsertedResource;
import com.example.demo.iface.dto.response.response.FieldViewCustomisationGottenResource;
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
	 * @return ResponseEntity<CustomisationUpsertedResource>
	 */
	@PostMapping("")
	public ResponseEntity<CustomisationUpsertedResource> upsert(@RequestBody UpsertCustomisationResource resource) {
		// 防腐處理 resource -> command
		UpsertCustomisationCommand command = BaseDataTransformer.transformData(resource,
				UpsertCustomisationCommand.class);
		customisationCommandService.upsert(command);
		return new ResponseEntity<>(new CustomisationUpsertedResource("200", "成功更新一筆個人化設定資料"), HttpStatus.OK);
	}

	/**
	 * 查詢表格顯示個人化設定資料
	 * 
	 * @param username  使用者帳號
	 * @param component Component 名稱
	 * @return ResponseEntity<FieldViewCustomisationGottenResource>
	 */
	@GetMapping("/fieldView")
	public ResponseEntity<FieldViewCustomisationGottenResource> getFieldViewCustomisation(@RequestParam String username,
			@RequestParam String component) {
		List<FieldViewCustomisationQueried> data = customisationQueryService.getCustomisation(username, component,
				"FieldView");
		return new ResponseEntity<>(new FieldViewCustomisationGottenResource("200", "查詢成功", data), HttpStatus.OK);
	}
}
