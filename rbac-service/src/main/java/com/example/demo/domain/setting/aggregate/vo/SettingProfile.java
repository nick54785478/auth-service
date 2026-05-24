package com.example.demo.domain.setting.aggregate.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SettingProfile {

	@Column(name = "name")
	private String name; // 名稱

	@Column(name = "description")
	private String description; // 描述

	/**
	 * 工廠方法 : 建立一筆設定
	 * 
	 * @param name        名稱
	 * @param description 描述
	 */
	public static SettingProfile of(String name, String description) {
		SettingProfile profile = new SettingProfile();
		profile.name = name;
		profile.description = description;
		return profile;
	}
}