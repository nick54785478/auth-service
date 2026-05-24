package com.example.demo.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.application.assembler.GroupAssembler;
import com.example.demo.application.shared.dto.GroupOptionQueried;
import com.example.demo.application.shared.dto.OptionQueried;
import com.example.demo.application.shared.dto.RoleOptionQueried;
import com.example.demo.application.shared.dto.UserOptionQueried;
import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.infra.repository.FunctionInfoRepository;
import com.example.demo.infra.repository.GroupInfoRepository;
import com.example.demo.infra.repository.RoleInfoRepository;
import com.example.demo.infra.repository.SettingRepository;
import com.example.demo.infra.repository.UserInfoRepository;
import com.example.demo.infra.spec.GetGroupOptionsSpecification;
import com.example.demo.shared.enums.YesNo;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OptionQueryService {

	private GroupAssembler groupAssembler;
	private RoleInfoRepository roleInfoRepository;
	private SettingRepository settingRepository;
	private UserInfoRepository userInfoRepository;
	private GroupInfoRepository groupInfoRepository;
	private FunctionInfoRepository functionInfoRepository;

	/**
	 * 查詢相關的設定
	 * 
	 * @param service 服務
	 * @param type    設定種類
	 * @return List<OptionQueried>
	 */
	public List<OptionQueried> getSettingTypes(String service, String type) {
		return settingRepository.findByServiceAndDataTypeAndActiveFlag(service, type, YesNo.Y).stream().map(setting -> {
			return new OptionQueried(setting.getId(), setting.getName(), setting.getCode());
		}).collect(Collectors.toList());
	}

	/**
	 * 查詢使用者資料 (AutoComplete)
	 * 
	 * @param str 使用者帳號字串
	 * @return List<UserOptionQueried>
	 */
	public List<UserOptionQueried> getUserOptions(String str) {
		return BaseDataTransformer.transformData(userInfoRepository.findByUsernameContaining(str),
				UserOptionQueried.class);

	}

	/**
	 * 查詢角色資料 (AutoComplete)
	 * 
	 * @param service 服務
	 * @param str     角色字串
	 * @return List<RoleOptionQueried>
	 */
	public List<RoleOptionQueried> getRoleOptions(String service, String str) {
		return BaseDataTransformer.transformData(roleInfoRepository.findAllWithSpecification(service, str),
				RoleOptionQueried.class);
	}

	/**
	 * 查詢角色資料 (DropDown)
	 * 
	 * @param service 服務
	 * @return List<OptionQueried>
	 */
	public List<OptionQueried> getRoleOptions(String service) {
		List<RoleInfo> roles = roleInfoRepository.findByScopeServiceAndActiveFlag(service, YesNo.Y);
		return roles.stream().map(RoleInfo::getType).distinct().map(type -> new OptionQueried(type, type))
				.collect(Collectors.toList());
	}

	/**
	 * 查詢群組資料 (AutoComplete)
	 * 
	 * @param service 服務
	 * @param keyword 關鍵字(用於模糊查詢)
	 * @return List<GroupOptionQueried>
	 */
	public List<GroupOptionQueried> getGroupOptions(String service, String keyword) {
		GetGroupOptionsSpecification specification = new GetGroupOptionsSpecification(service, keyword);
		List<GroupInfo> groups = groupInfoRepository.findAll(specification.toSpecification());
		return groupAssembler.transformGroupOptions(groups);
	}

	/**
	 * 查詢群組資料 (DropDown)
	 * 
	 * @param service 服務
	 * @return List<OptionQueried>
	 */
	public List<OptionQueried> getGroupOptions(String service) {
		List<GroupInfo> groups = groupInfoRepository.findByScopeServiceAndActiveFlag(service, YesNo.Y);
		return groups.stream().map(GroupInfo::getType).distinct().map(type -> new OptionQueried(type, type))
				.collect(Collectors.toList());
	}

	/**
	 * 查詢功能資料 (DropDown)
	 * 
	 * @param service 服務
	 * @return List<OptionQueried>
	 */
	public List<OptionQueried> getFunctionOptions(String service) {
		List<FunctionInfo> groups = functionInfoRepository.findByScopeServiceAndActiveFlag(service, YesNo.Y);
		return groups.stream().map(FunctionInfo::getType).distinct().map(type -> new OptionQueried(type, type))
				.collect(Collectors.toList());
	}
}
