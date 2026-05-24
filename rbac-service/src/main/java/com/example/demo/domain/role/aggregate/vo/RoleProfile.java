package com.example.demo.domain.role.aggregate.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 角色描述
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleProfile {

	@Column(name = "name", nullable = false)
	private String name; // 角色名稱

	@Column(name = "description")
	private String description; // 角色描述

	/**
	 * 工廠類方法 : 填充 Role Profile
	 * 
	 * @param name        角色名稱
	 * @param description 角色描述
	 */
	public static RoleProfile of(String name, String description) {
		RoleProfile profile = new RoleProfile();
		profile.name = name;
		profile.description = description;
		return profile;
	}
}