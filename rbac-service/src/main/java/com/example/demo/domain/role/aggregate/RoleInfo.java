package com.example.demo.domain.role.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.domain.role.aggregate.entity.RoleFunction;
import com.example.demo.domain.role.command.CreateOrUpdateRoleCommand;
import com.example.demo.domain.role.command.CreateRoleCommand;
import com.example.demo.domain.role.command.UpdateRoleCommand;
import com.example.demo.shared.enums.YesNo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 角色表
 */
@Getter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role_info")
@EntityListeners(AuditingEntityListener.class)
public class RoleInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String service;

	private String code; // 角色 Code

	private String name;

	private String type; // 權限種類

	private String description; // 敘述

	// 使用懶加載，避免 N+1 query 效能問題
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	private List<RoleFunction> functions = new ArrayList<>(); // 角色所屬功能

	@Enumerated(EnumType.STRING)
	private YesNo activeFlag = YesNo.Y; // 是否有效

	/**
	 * 新增一筆角色資料
	 * 
	 * @param command
	 */
	public void create(CreateRoleCommand command) {
		this.code = command.getCode();
		this.service = command.getService();
		this.name = command.getName();
		this.description = command.getDescription();
		this.type = command.getType();
		this.activeFlag = YesNo.Y;
	}

	/**
	 * 新增一筆角色資料
	 * 
	 * @param command
	 */
	public void create(CreateOrUpdateRoleCommand command) {
		this.service = command.getService();
		this.code = command.getCode();
		this.name = command.getName();
		this.description = command.getDescription();
		this.type = command.getType();
		this.activeFlag = YesNo.Y;
	}

	/**
	 * 更新一筆角色資料
	 * 
	 * @param command
	 */
	public void update(UpdateRoleCommand command) {
		this.service = command.getService();
		this.code = command.getCode();
		this.name = command.getName();
		this.type = command.getType();
		this.description = command.getDescription();
	}

	/**
	 * 更新一筆角色資料
	 * 
	 * @param command
	 */
	public void update(CreateOrUpdateRoleCommand command) {
		this.id = command.getId();
		this.service = command.getService();
		this.code = command.getCode();
		this.name = command.getName();
		this.type = command.getType();
		this.description = command.getDescription();
		this.activeFlag = YesNo.valueOf(command.getActiveFlag());
	}

	/**
	 * 更新角色 Function 清單，使其有權限執行相關動作
	 * 
	 * @param roleFunctions 更新後的使用者角色清單
	 */
	public void updateFunctions(List<RoleFunction> roleFunctions) {
		// DB 內的角色 ID Map
		Map<Long, RoleFunction> existMap = this.functions.stream()
				.collect(Collectors.toMap(RoleFunction::getFunctionId, Function.identity()));

		// 新資料沒有但舊資料有 => 刪除
		List<RoleFunction> result = this.functions.stream()
				.filter(existingFunction -> roleFunctions.stream()
						.noneMatch(newFunction -> newFunction.getFunctionId().equals(existingFunction.getFunctionId())))
				.peek(function -> {
					function.delete();
				}) // peek 在收集到清單之前執行
				.collect(Collectors.toList());
		// 遍歷使用者的角色資料蒐集
		roleFunctions.stream().forEach(e -> {
			// functionId 對不到 --> 新資料中有但舊資料沒有的資料 => 新增
			if (Objects.isNull(existMap.get(e.getFunctionId()))) {
				result.add(e);
			} else {
				// 有對到 --> 新蓋舊
				RoleFunction old = existMap.get(e.getFunctionId());
				e.update(old.getId(), old.getRoleId(), old.getFunctionId());
				result.add(e);
			}
		});
		this.functions = result;

//		// 移除 functions 中不存在於 Role Functions 的項目
//		this.functions.removeIf(
//				existingFunction -> roleFunctions.stream().noneMatch(newFunc -> newFunc.equals(existingFunction)));
//		// 增加新的 Role Function
//		roleFunctions.stream().forEach(newFunction -> {
//			if (!functions.contains(newFunction)) {
//				functions.add(newFunction);
//			}
//		});
	}

	/**
	 * 刪除角色功能資料 (ActiveFlag = "N")
	 */
	public void delete() {
		this.activeFlag = YesNo.N;
	}

}
