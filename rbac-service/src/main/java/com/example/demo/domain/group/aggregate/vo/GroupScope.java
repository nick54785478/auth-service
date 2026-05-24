package com.example.demo.domain.group.aggregate.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 群組範圍
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupScope {

	@Column(name = "service", nullable = false)
	private String service; // 服務，可根據個別服務建立該服務適用的群組

	@Column(name = "code", nullable = false)
	private String code; // 群組代號

	
	/**
	 * 工廠類方法: 填充 GroupScope
	 * 
	 * @param service 服務
	 * @param code    角色代碼
	 */
	public static GroupScope of(String service, String code) {
		GroupScope scope = new GroupScope();
		scope.service = service;
		scope.code = code;
		return scope;
	}
}