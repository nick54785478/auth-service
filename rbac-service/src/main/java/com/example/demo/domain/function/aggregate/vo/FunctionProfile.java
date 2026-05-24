package com.example.demo.domain.function.aggregate.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 功能描述檔
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FunctionProfile {

	@Column(name = "name", nullable = false)
	private String name; // 功能名稱

	@Column(name = "description")
	private String description; // 功能描述

	/**
	 * 工廠類方法 : 填充 FunctionProfile
	 * 
	 * @param name        角色名稱
	 * @param description 角色描述
	 */
	public static FunctionProfile of(String name, String description) {
		FunctionProfile profile = new FunctionProfile();
		profile.name = name;
		profile.description = description;
		return profile;
	}
}