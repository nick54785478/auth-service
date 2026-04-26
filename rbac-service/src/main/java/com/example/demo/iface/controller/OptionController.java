package com.example.demo.iface.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.service.OptionQueryService;
import com.example.demo.application.shared.dto.GroupOptionQueried;
import com.example.demo.application.shared.dto.OptionQueried;
import com.example.demo.application.shared.dto.RoleOptionQueried;
import com.example.demo.application.shared.dto.UserOptionQueried;
import com.example.demo.iface.dto.response.response.GroupOptionsGottenResource;
import com.example.demo.iface.dto.response.response.OptionGottenResource;
import com.example.demo.iface.dto.response.response.RoleOptionsGottenResource;
import com.example.demo.iface.dto.response.response.UserOptionGottenResource;

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
	public ResponseEntity<UserOptionGottenResource> getUserOptions(@RequestParam("queryStr") String str) {
		List<UserOptionQueried> data = optionQueryService.getUserOptions(str);
		return new ResponseEntity<>(new UserOptionGottenResource("200", "查詢成功", data), HttpStatus.OK);
	}

	/**
	 * 查詢角色相關的 AutoComplete 資料
	 * 
	 * @param service 服務
	 * @param str     角色相關字串 return ResponseEntity<List<RoleOptionQueriedResource>>
	 * @return ResponseEntity<RoleOptionsGottenResource>
	 */
	@GetMapping("/roles")
	public ResponseEntity<RoleOptionsGottenResource> getRoleOptions(@RequestParam String service,
			@RequestParam("queryStr") String str) {
		List<RoleOptionQueried> data = optionQueryService.getRoleOptions(service, str);
		return new ResponseEntity<>(new RoleOptionsGottenResource("200", "查詢成功", data), HttpStatus.OK);
	}

	/**
	 * 查詢角色種類 (DropDown 下拉式選單)
	 * 
	 * @param service 服務
	 * @return ResponseEntity<OptionGottenResource>
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
	 * @return ResponseEntity<GroupOptionsGottenResource>
	 */
	@GetMapping("/groups")
	public ResponseEntity<GroupOptionsGottenResource> getGroupOptions(@RequestParam String service,
			@RequestParam("queryStr") String str) {
		List<GroupOptionQueried> data = optionQueryService.getGroupOptions(service, str);
		return new ResponseEntity<>(new GroupOptionsGottenResource("200", "查詢成功", data), HttpStatus.OK);
	}

	/**
	 * 查詢群組種類 (DropDown 下拉式選單)
	 * 
	 * @param service 服務
	 * @return ResponseEntity<OptionGottenResource>
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
	 * @return ResponseEntity<OptionGottenResource>
	 */
	@GetMapping("/functions/types")
	public ResponseEntity<OptionGottenResource> getFunctionTypeOptions(@RequestParam String service) {
		List<OptionQueried> data = optionQueryService.getFunctionOptions(service);
		return new ResponseEntity<>(new OptionGottenResource("200", "查詢成功", data), HttpStatus.OK);
	}

}
