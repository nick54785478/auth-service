package com.example.demo.iface.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.service.OptionQueryService;
import com.example.demo.application.shared.dto.OptionQueried;
import com.example.demo.iface.dto.out.GroupOptionQueriedResource;
import com.example.demo.iface.dto.out.OptionGottenResource;
import com.example.demo.iface.dto.out.RoleOptionQueriedResource;
import com.example.demo.iface.dto.out.UserOptionQueriedResource;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/options")
public class OptionController {

	private OptionQueryService optionQueryService;

	/**
	 * 查詢相關的設定 (下拉式選單)
	 * 
	 * @param type 設定種類
	 * @return ResponseEntity<OptionGottenResource>
	 */
	@GetMapping("/query")
	public ResponseEntity<OptionGottenResource> query(@RequestParam String service, @RequestParam String type) {
		List<OptionQueried> data = optionQueryService.getSettingTypes(service, type);
		return new ResponseEntity<>(new OptionGottenResource("200", "查詢成功", data), HttpStatus.OK);
	}

	/**
	 * 查詢使用者相關的設定 (AutoComplete)
	 * 
	 * @param str 使用者相關字串 return ResponseEntity<List<UserOptionQueriedResource>>
	 */
	@GetMapping("/getUserOptions")
	public ResponseEntity<List<UserOptionQueriedResource>> getUserOptions(@RequestParam("queryStr") String str) {
		return new ResponseEntity<>(BaseDataTransformer.transformData(optionQueryService.getUserOptions(str),
				UserOptionQueriedResource.class), HttpStatus.OK);
	}

	/**
	 * 查詢角色相關的 AutoComplete 資料
	 * 
	 * @param service 服務
	 * @param str     角色相關字串 return ResponseEntity<List<RoleOptionQueriedResource>>
	 */
	@GetMapping("/roles")
	public ResponseEntity<List<RoleOptionQueriedResource>> getRoleOptions(@RequestParam String service,
			@RequestParam("queryStr") String str) {
		return new ResponseEntity<>(BaseDataTransformer.transformData(optionQueryService.getRoleOptions(service, str),
				RoleOptionQueriedResource.class), HttpStatus.OK);
	}

	/**
	 * 查詢角色種類 (DropDown 下拉式選單)
	 * 
	 * @param service 服務
	 */
	@GetMapping("/roles/types")
	public ResponseEntity<OptionGottenResource> getRoleTypeOptions(@RequestParam String service) {
		List<OptionQueried> data = optionQueryService.getRoleOptions(service);
		return new ResponseEntity<>(new OptionGottenResource("200", "查詢成功", data), HttpStatus.OK);
	}

	/**
	 * 查詢群組相關的 AutoComplete 資料
	 * 
	 * @param service 服務
	 * @param str     群組相關字串 return ResponseEntity<List<GroupOptionQueriedResource>>
	 */
	@GetMapping("/groups")
	public ResponseEntity<List<GroupOptionQueriedResource>> getGroupOptions(@RequestParam String service,
			@RequestParam("queryStr") String str) {
		return new ResponseEntity<>(BaseDataTransformer.transformData(optionQueryService.getGroupOptions(service, str),
				GroupOptionQueriedResource.class), HttpStatus.OK);
	}

	/**
	 * 查詢群組種類 (DropDown 下拉式選單)
	 * 
	 * @param service 服務
	 */
	@GetMapping("/groups/types")
	public ResponseEntity<OptionGottenResource> getGroupTypeOptions(@RequestParam String service) {
		List<OptionQueried> data = optionQueryService.getGroupOptions(service);
		return new ResponseEntity<>(new OptionGottenResource("200", "查詢成功", data), HttpStatus.OK);
	}

	/**
	 * 查詢群組種類 (DropDown 下拉式選單)
	 * 
	 * @param service 服務
	 */
	@GetMapping("/functions/types")
	public ResponseEntity<OptionGottenResource> getFunctionTypeOptions(@RequestParam String service) {
		List<OptionQueried> data = optionQueryService.getFunctionOptions(service);
		return new ResponseEntity<>(new OptionGottenResource("200", "查詢成功", data), HttpStatus.OK);
	}


}
