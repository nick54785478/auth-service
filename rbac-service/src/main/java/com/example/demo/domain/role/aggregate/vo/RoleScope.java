package com.example.demo.domain.role.aggregate.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 角色範圍
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleScope {

	@Column(name = "service", nullable = false)
	private String service; // 服務，可根據個別服務建立該服務適用的角色

	@Column(name = "code", nullable = false)
	private String code; // 角色代碼

	/**
	 * 工廠類方法: 填充 RoleScope
	 * 
	 * @param service 服務
	 * @param code    角色代碼
	 */
	public static RoleScope of(String service, String code) {
		RoleScope scope = new RoleScope();
		scope.service = service;
		scope.code = code;
		return scope;
	}
}