package com.example.demo.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.service.SettingService;
import com.example.demo.domain.setting.command.CreateSettingCommand;
import com.example.demo.domain.setting.command.UpdateSettingCommand;
import com.example.demo.infra.exception.ValidationException;
import com.example.demo.infra.repository.SettingRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class SettingCommandService {

	private SettingService settingService;
	private SettingRepository settingRepository;

	/**
	 * 建立設定
	 * 
	 * @param command
	 */
	public void create(CreateSettingCommand command) {
		// 檢查設定
		settingService.create(command);
	}

	/**
	 * 修改設定
	 * 
	 * @param command
	 */
	public void update(UpdateSettingCommand command) {
		// 檢查設定
		settingService.update(command);
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
			throw new ValidationException("VALIDATION_FAILED", "查無此資料，刪除失敗");
		});
	}
}
