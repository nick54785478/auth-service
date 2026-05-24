package com.example.demo.domain.group.aggregate.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 群組描述檔
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupProfile {

	@Column(name = "name", nullable = false)
	private String name; // 名稱

	@Column(name = "description")
	private String description; // 敘述

	/**
	 * 工廠類方法 : 填充 GroupProfile
	 * 
	 * @param name        角色名稱
	 * @param description 角色描述
	 */
	public static GroupProfile of(String name, String description) {
		GroupProfile profile = new GroupProfile();
		profile.name = name;
		profile.description = description;
		return profile;
	}
}