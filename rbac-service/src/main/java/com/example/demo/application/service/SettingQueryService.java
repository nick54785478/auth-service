package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.application.assembler.SettingAssembler;
import com.example.demo.application.shared.dto.SettingQueried;
import com.example.demo.application.shared.query.GetSettingSummaryQuery;
import com.example.demo.domain.setting.aggregate.Setting;
import com.example.demo.domain.setting.repository.SettingRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SettingQueryService {

	private SettingAssembler assembler;
	private SettingRepository settingRepository;

	/**
	 * 根據條件查詢 Setting
	 * 
	 * @param query {@link GetSettingSummaryQuery}
	 * @return List<SettingQueried> 設定清單
	 */
	public List<SettingQueried> summary(GetSettingSummaryQuery query) {
		List<Setting> settingList = settingRepository.summary(query.getService(), query.getDataType(), query.getType(),
				query.getName(), query.getActiveFlag());
		return assembler.transformSettings(settingList);
	}

}
