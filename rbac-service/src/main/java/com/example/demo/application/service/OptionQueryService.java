package com.example.demo.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.application.shared.dto.GroupOptionQueried;
import com.example.demo.application.shared.dto.OptionQueried;
import com.example.demo.application.shared.dto.RoleOptionQueried;
import com.example.demo.application.shared.dto.UserOptionQueried;
import com.example.demo.infra.repository.GroupInfoRepository;
import com.example.demo.infra.repository.RoleInfoRepository;
import com.example.demo.infra.repository.SettingRepository;
import com.example.demo.infra.repository.UserInfoRepository;
import com.example.demo.shared.enums.YesNo;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OptionQueryService {

	private RoleInfoRepository roleInfoRepository;
	private SettingRepository settingRepository;
	private UserInfoRepository userInfoRepository;
	private GroupInfoRepository groupInfoRepository;

	/**
	 * 查詢相關的設定
	 * 
	 * @param service 服務
	 * @param type    設定種類
	 * @return List<OptionQueried>
	 */
	public List<OptionQueried> getSettingTypes(String service, String type) {
		return settingRepository.findByServiceAndDataTypeAndActiveFlag(service, type, YesNo.Y).stream().map(setting -> {
			return new OptionQueried(setting.getId(), setting.getType(), setting.getType());
		}).collect(Collectors.toList());
	}

	/**
	 * 查詢使用者資料 (AutoComplete)
	 * 
	 * @param str 使用者字串
	 * @return List<UserOptionQueried>
	 */
	public List<UserOptionQueried> getUserOptions(String str) {
		return BaseDataTransformer.transformData(userInfoRepository.findAllWithSpecification(str),
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
	 * 查詢群組資料 (AutoComplete)
	 * 
	 * @param service 服務
	 * @param str     群組字串
	 * @return List<GroupOptionQueried>
	 */
	public List<GroupOptionQueried> getGroupOptions(String service, String str) {
		return BaseDataTransformer.transformData(groupInfoRepository.findAllWithSpecification(service, str),
				GroupOptionQueried.class);
	}
}
