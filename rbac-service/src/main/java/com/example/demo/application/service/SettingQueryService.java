package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.application.shared.dto.SettingQueried;
import com.example.demo.domain.setting.aggregate.Setting;
import com.example.demo.infra.repository.SettingRepository;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SettingQueryService {

	private SettingRepository settingRepository;

	/**
	 * 根據條件查詢 Setting
	 * 
	 * @param service    服務
	 * @param dataType   資料種類
	 * @param type       設定類別
	 * @param name       名稱
	 * @param activeFlag 是否生效
	 * @return List<SettingQueried> 設定清單
	 */
	public List<SettingQueried> query(String service, String dataType, String type, String name, String activeFlag) {
		List<Setting> settingList = settingRepository.findAllWithSpecification(service, dataType, type, name,
				activeFlag);
		return BaseDataTransformer.transformData(settingList, SettingQueried.class);
	}

}
