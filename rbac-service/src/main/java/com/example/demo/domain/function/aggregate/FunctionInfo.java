package com.example.demo.domain.function.aggregate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.domain.function.aggregate.vo.ActionType;
import com.example.demo.domain.function.command.CreateFunctionCommand;
import com.example.demo.domain.function.command.CreateOrUpdateFunctionCommand;
import com.example.demo.shared.enums.YesNo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "function_info")
@EntityListeners(AuditingEntityListener.class)
public class FunctionInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String service;

	private String type; // 種類

	@Column(name = "action_type")
	@Enumerated(EnumType.STRING)
	private ActionType actionType; // 動作種類

	private String code; // Code

	private String name; // Code

	private String description; // 敘述

	@Column(name = "active_flag")
	@Enumerated(EnumType.STRING)
	private YesNo activeFlag = YesNo.Y; // 是否有效

	/**
	 * 建立一筆功能資料
	 * 
	 * @param command CreateFunctionCommand
	 */
	public void create(CreateFunctionCommand command) {
		this.service = command.getService();
		this.type = command.getType();
		this.name = command.getName();
		this.actionType = ActionType.fromLabel(command.getActionType());
		this.code = command.getCode();
		this.description = command.getDescription();
		this.activeFlag = YesNo.Y;
	}

	/**
	 * 新增一筆功能資料
	 * 
	 * @param command CreateOrUpdateFunctionCommand
	 */
	public void create(CreateOrUpdateFunctionCommand command) {
		this.service = command.getService();
		this.code = command.getCode();
		this.name = command.getName();
		this.description = command.getDescription();
		this.actionType = ActionType.fromLabel(command.getActionType());
		this.type = command.getType();
		this.activeFlag = YesNo.Y;
	}

	/**
	 * 更新一筆功能資料
	 * 
	 * @param command CreateOrUpdateFunctionCommand
	 */
	public void update(CreateOrUpdateFunctionCommand command) {
		this.id = command.getId();
		this.service = command.getService();
		this.code = command.getCode();
		this.name = command.getName();
		this.type = command.getType();
		this.actionType = ActionType.fromLabel(command.getActionType());
		this.description = command.getDescription();
		this.activeFlag = YesNo.valueOf(command.getActiveFlag());
	}

	/**
	 * 刪除使用者資料 (ActiveFlag = "N")
	 */
	public void delete() {
		this.activeFlag = YesNo.N;
	}

}
