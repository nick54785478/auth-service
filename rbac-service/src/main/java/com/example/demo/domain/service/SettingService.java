package com.example.demo.domain.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.example.demo.application.shared.dto.CreateSettingCommand;
import com.example.demo.application.shared.dto.UpdateSettingCommand;
import com.example.demo.domain.setting.aggregate.Setting;
import com.example.demo.domain.setting.aggregate.vo.SettingProfile;
import com.example.demo.domain.setting.aggregate.vo.SettingScope;
import com.example.demo.domain.setting.repository.SettingRepository;
import com.example.demo.infra.exception.ValidationException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class SettingService {

	private SettingRepository settingRepository;

	/**
	 * 建立設定
	 * 
	 * @param command {@link CreateSettingCommand}
	 */
	public void create(CreateSettingCommand command) {
		// 領域檢核:
		this.checkSetting(command.getType(), command.getPriorityNo());

		SettingScope scope = SettingScope.of(command.getService(), command.getCode());
		SettingProfile profile = SettingProfile.of(command.getName(), command.getDescription());

		// 進行新增動作
		Setting setting = Setting.create(scope, profile, command.getDataType(), command.getType(), command.getValue(),
				command.getPriorityNo());
		settingRepository.save(setting);
	}

	/**
	 * 修改設定
	 * 
	 * @param command {@link UpdateSettingCommand}
	 */
	public void update(UpdateSettingCommand command) {
		// 領域檢核: 檢查資料
		this.checkSetting(command.getType(), command.getPriorityNo());

		settingRepository.findById(command.getId()).ifPresentOrElse(setting -> {
			SettingProfile profile = SettingProfile.of(command.getName(), command.getDescription());
			setting.update(profile, command.getDataType(), command.getType(), command.getValue(),
					command.getPriorityNo(), command.getActiveFlag());
			settingRepository.save(setting);
		}, () -> {
			throw new ValidationException("VALIDATE_FAILED", "查無此資料，更新失敗");
		});
	}

	/**
	 * 刪除特定 id 的 Setting 資料
	 * 
	 * @param id Setting id
	 */
	public void delete(Long id) {
		settingRepository.findById(id).ifPresentOrElse(setting -> {
			setting.delete();
			settingRepository.save(setting);
		}, () -> {
			log.error("查無此資料，ID:{} 刪除失敗 ", id);
			throw new ValidationException("VALIDATE_FAILED", "查無此資料，刪除失敗");
		});
	}

	/**
	 * 進行領域檢核
	 * 
	 * @param type       種類
	 * @param priorityNo 排序
	 */
	private void checkSetting(String type, Integer priorityNo) {
		if (StringUtils.equals(type, "CONFIGURE") && priorityNo != 0L) {
			throw new ValidationException("VALIDATE_FAILED", "資料配置有誤，Configure 的排序號需為 0");
		}

		if (StringUtils.equals(type, "DATA") && priorityNo == 0L) {
			throw new ValidationException("VALIDATE_FAILED", "資料配置有誤，Data 的排序號需大於 0");
		}

	}

}
