package com.example.demo.application.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.application.shared.dto.SettingQueried;
import com.example.demo.domain.setting.aggregate.Setting;
import com.example.demo.domain.setting.aggregate.vo.SettingProfile;
import com.example.demo.domain.setting.aggregate.vo.SettingScope;

/**
 * 設定資料轉換器 (Setting Assembler)
 */
@Component
public class SettingAssembler {

	/**
	 * 轉換設定資料
	 * 
	 * @param setting 設定
	 * @return {@link SettingQueried}
	 */
	public SettingQueried transformSetting(Setting setting) {
		if (setting == null) {
			return null;
		}
		SettingScope scope = setting.getScope();
		SettingProfile profile = setting.getProfile();
		return new SettingQueried(setting.getId(), scope.getService(), setting.getDataType(), setting.getType(),
				profile.getName(), scope.getCode(), setting.getValue(), profile.getDescription(),
				setting.getPriorityNo(), setting.getActiveFlag());
	}

	/**
	 * 轉換設定資料清單
	 * 
	 * @param setting {@link Setting} 清單
	 * @return {@link SettingQueried} 清單
	 */
	public List<SettingQueried> transformSettings(List<Setting> settings) {
		return settings.stream().map(this::transformSetting).collect(Collectors.toList());
	}
}
