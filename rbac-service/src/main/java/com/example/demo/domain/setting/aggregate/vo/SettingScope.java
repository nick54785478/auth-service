package com.example.demo.domain.setting.aggregate.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SettingScope {

	@Column(name = "service", nullable = false)
	private String service;

	@Column(name = "code", nullable = false)
	private String code;

	public static SettingScope of(String service, String code) {
		SettingScope scope = new SettingScope();
		scope.service = service;
		scope.code = code;
		return scope;
	}
}