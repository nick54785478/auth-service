package com.example.demo.domain.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.example.demo.domain.setting.aggregate.Setting;
import com.example.demo.domain.setting.command.CreateSettingCommand;
import com.example.demo.domain.setting.command.UpdateSettingCommand;
import com.example.demo.infra.exception.ValidationException;
import com.example.demo.infra.repository.SettingRepository;

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
	 * @param command
	 */
	public void create(CreateSettingCommand command) {
		// 領域檢核:
		this.checkSetting(command.getType(), command.getPriorityNo());

		// 進行新增動作
		Setting setting = new Setting();
		setting.create(command);
		settingRepository.save(setting);
	}

	/**
	 * 修改設定
	 * 
	 * @param command
	 */
	public void update(UpdateSettingCommand command) {
		// 領域檢核:
		this.checkSetting(command.getType(), command.getPriorityNo());

		// 檢查資料
		settingRepository.findById(command.getId()).ifPresentOrElse(setting -> {
			setting.update(command);
			settingRepository.save(setting);
		}, () -> {
			throw new ValidationException("VALIDATION_FAILED", "查無此資料，更新失敗");
		});
	}

	/**
	 * 刪除特定 id 的 Setting 資料
	 * 
	 * @param id
	 */
	public void delete(Long id) {
		settingRepository.findById(id).ifPresentOrElse(setting -> {
			setting.delete();
			settingRepository.save(setting);
		}, () -> {
			log.error("查無此資料，ID:{} 刪除失敗 ", id);
			throw new ValidationException("VALIDATION_FAILED", "查無此資料，刪除失敗");
		});
	}

	/**
	 * 進行領域檢核
	 * 
	 * @param command
	 */
	private void checkSetting(String type, Integer priorityNo) {
		if (StringUtils.equals(type, "CONFIGURE") && priorityNo != 0L) {
			throw new ValidationException("VALIDATION_FAILED", "資料配置有誤，Configure 的排序號需為 0");
		}

		if (StringUtils.equals(type, "DATA") && priorityNo == 0L) {
			throw new ValidationException("VALIDATION_FAILED", "資料配置有誤，Data 的排序號需大於 0");
		}

	}

}
