package com.example.demo.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class SettingCommandService {

	private SettingRepository settingRepository;

	/**
	 * 建立設定
	 * 
	 * @param command {@link CreateSettingCommand}
	 */
	public void create(CreateSettingCommand command) {
		// 檢查設定
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

		settingRepository.findById(command.getId()).ifPresentOrElse(setting -> {
			SettingProfile profile = SettingProfile.of(command.getName(), command.getDescription());
			setting.update(profile, command.getDataType(), command.getType(), command.getValue(),
					command.getPriorityNo(), command.getActiveFlag());
			settingRepository.save(setting);
		}, () -> {
			throw new ValidationException("VALIDATE_FAILED", "查無此資料，更新失敗");
		});

//		// 檢查設定
//		settingService.update(command);
	}

	/**
	 * 刪除特定資料
	 * 
	 * @param id
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
}
