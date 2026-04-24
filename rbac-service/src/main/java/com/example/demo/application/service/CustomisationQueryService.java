package com.example.demo.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.example.demo.application.shared.dto.FieldViewCustomisationQueried;
import com.example.demo.domain.customisation.aggregate.Customisation;
import com.example.demo.infra.repository.CustomisationRepository;
import com.example.demo.util.JsonParseUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomisationQueryService {

	private CustomisationRepository customisationRepository;

	/**
	 * 取得表格欄位顯示設定
	 * 
	 * @param username  使用者名稱
	 * @param component Component
	 * @param type      種類
	 * @return 表格欄位設定
	 */
	public List<FieldViewCustomisationQueried> getCustomisation(String username, String component, String type) {
		Customisation customisation = customisationRepository
				.findByUsernameAndComponentAndType(username, component, type).orElse(null);
		if (!this.checkValue(customisation)) {
			return new ArrayList<>();
		}
		return JsonParseUtil.unserializeArrayOfObject(customisation.getValue(), FieldViewCustomisationQueried.class);
	}

	/**
	 * 檢查個人化配置資料
	 * 
	 * @param customisation 個人化配置
	 * @return 是否合乎規則
	 */
	private boolean checkValue(Customisation customisation) {
		if (Objects.isNull(customisation)) {
			return false;
		}
		if (StringUtils.isBlank(customisation.getValue())) {
			return false;
		}
		return true;
	}
}
