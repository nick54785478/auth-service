package com.example.demo.domain.function.aggregate.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 功能範圍 (業務自然鍵)
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FunctionScope {

	@Column(name = "service", nullable = false)
	private String service; // 服務，可根據個別服務建立該服務適用功能

	@Column(name = "code", nullable = false)
	private String code; // 功能代碼

	/**
	 * 工廠類方法: 填充 FunctionScope
	 * 
	 * @param service 服務
	 * @param code    功能代碼
	 */
	public static FunctionScope of(String service, String code) {
		FunctionScope scope = new FunctionScope();
		scope.service = service;
		scope.code = code;
		return scope;
	}
}