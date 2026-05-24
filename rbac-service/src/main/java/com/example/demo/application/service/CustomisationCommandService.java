package com.example.demo.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.customisation.aggregate.Customisation;
import com.example.demo.domain.customisation.aggregate.vo.CustomisationScope;
import com.example.demo.domain.customisation.command.UpsertCustomisationCommand;
import com.example.demo.infra.repository.CustomisationRepository;
import com.example.demo.util.JsonParseUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class CustomisationCommandService {

	private CustomisationRepository customisationRepository;

	/**
	 * 更新個人客製化設置 (Upsert 機制)
	 * 
	 * @param command {@link UpsertCustomisationCommand}
	 */
	@Transactional
	public void upsert(UpsertCustomisationCommand command) {
		// 1. 在 Application 層處理技術細節 (JSON 序列化)
		String jsonValue = JsonParseUtil.serialize(command.getValueList());

		// 2. 透過 Repository 查詢是否存在 (注意：方法名稱需因應 Scope 調整)
		Customisation customisation = customisationRepository
				.findByScopeUsernameAndScopeComponentAndScopeType(command.getUsername(), command.getComponent(),
						command.getType())
				.map(existing -> {
					// 找到舊資料 => 呼叫領域行為只更新 Value
					existing.updateValue(jsonValue);
					return existing;
				}).orElseGet(() -> {
					// 找不到舊資料 => 建立新的 Scope 與實體
					CustomisationScope scope = CustomisationScope.of(command.getUsername(), command.getComponent(),
							command.getType());
					return Customisation.create(scope, jsonValue);
				});

		// 3. 儲存 (JPA 會自動判斷是 INSERT 還是 UPDATE)
		customisationRepository.save(customisation);
	}

}
