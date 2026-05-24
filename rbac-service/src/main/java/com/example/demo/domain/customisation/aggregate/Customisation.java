package com.example.demo.domain.customisation.aggregate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.domain.customisation.aggregate.vo.CustomisationScope;
import com.example.demo.shared.enums.YesNo;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 個人化配置 Aggregate Root
 */
@Entity
@Getter
@ToString
@Table(name = "customisation", uniqueConstraints = {
		@UniqueConstraint(name = "uk_customisation_scope", columnNames = { "username", "component", "type", "name" }) })
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 隱藏預設建構子，保護 Aggregate
public class Customisation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private CustomisationScope scope; // 封裝 username, component, type, name

	@Column(name = "value", columnDefinition = "TEXT") // JSON 字串通常較長，建議 DB 欄位設為 TEXT 或長 VARCHAR
	private String value;

	@Enumerated(EnumType.STRING)
	@Column(name = "active_flag")
	private YesNo activeFlag = YesNo.Y; // 是否生效

	/**
	 * 純粹的工廠方法
	 * 
	 * @param scope 個人化配置範圍
	 * @param value 已在 Application 層序列化好的 JSON 字串
	 */
	public static Customisation create(CustomisationScope scope, String value) {
		Customisation customisation = new Customisation();
		customisation.scope = scope;
		customisation.value = value;
		customisation.activeFlag = YesNo.Y;
		return customisation;
	}

	/**
	 * 領域行為：更新配置內容
	 * 
	 * @param newValue 新的 JSON 配置字串
	 */
	public void updateValue(String newValue) {
		this.value = newValue;
	}

	/**
	 * 領域行為：刪除 / 停用配置
	 */
	public void delete() {
		this.activeFlag = YesNo.N;
	}
}
