package com.example.demo.domain.function.aggregate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.domain.function.aggregate.vo.ActionType;
import com.example.demo.domain.function.aggregate.vo.FunctionProfile;
import com.example.demo.domain.function.aggregate.vo.FunctionScope;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 功能表
 */
@Entity
@Getter
@ToString
@AllArgsConstructor
@Table(name = "function_info", uniqueConstraints = {
		@UniqueConstraint(name = "uk_function_service_code", columnNames = { "service", "code" }) })
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FunctionInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private FunctionScope scope; // 替換原本的 service, code

	@Column(name = "action_type")
	@Enumerated(EnumType.STRING)
	private ActionType actionType; // 動作種類

	@Column(name = "type")
	private String type; // 種類

	@Embedded
	private FunctionProfile profile; // 替換原本的 name, description

	@Column(name = "active_flag")
	@Enumerated(EnumType.STRING)
	private YesNo activeFlag = YesNo.Y; // 是否有效

	/**
	 * 工廠方法: 新增一筆功能資料
	 * 
	 * @param scope   功能範圍
	 * @param profile 功能描述
	 * @param type    功能種類
	 * @param action  動作種類
	 * @return 群組資料
	 */
	public static FunctionInfo create(FunctionScope scope, FunctionProfile profile, String type,
			String actionType) {
		FunctionInfo function = new FunctionInfo();
		function.scope = scope;
		function.profile = profile;
		function.type = type;
		function.actionType = ActionType.fromLabel(actionType);
		function.activeFlag = YesNo.Y;
		return function;
	}

	/**
	 * 更新一筆功能資料
	 * 
	 * @param scope      功能範圍
	 * @param profile    功能描述
	 * @param type       功能種類
	 * @param action     動作種類
	 * @param activeFlag 是否生效
	 */
	public void update(FunctionScope scope, FunctionProfile profile, String type, String actionType,
			String activeFlag) {
		this.scope = scope;
		this.profile = profile;
		this.type = type;
		this.actionType = ActionType.fromLabel(actionType);
		this.activeFlag = YesNo.valueOf(activeFlag);
	}

	/**
	 * 刪除使用者資料 (ActiveFlag = "N")
	 */
	public void delete() {
		this.activeFlag = YesNo.N;
	}
	
}
