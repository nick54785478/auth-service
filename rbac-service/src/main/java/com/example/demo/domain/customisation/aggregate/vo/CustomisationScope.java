package com.example.demo.domain.customisation.aggregate.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 個人化配置範圍 (業務自然鍵)
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomisationScope {

	@Column(name = "username", nullable = false)
	private String username; // 帳號

	@Column(name = "component", nullable = false)
	private String component; // Component 名稱

	@Column(name = "type", nullable = false)
	private String type; // 種類 (例如: TABLE_FIELD)

	/**
	 * 工廠類方法: 填充 CustomisationScope
	 * 
	 * @param username  使用者帳號
	 * @param component 前端 Component 名稱
	 * @param type      種類
	 * @return {@link CustomisationScope}
	 */
	public static CustomisationScope of(String username, String component, String type) {
		CustomisationScope scope = new CustomisationScope();
		scope.username = username;
		scope.component = component;
		scope.type = type;
		return scope;
	}
}