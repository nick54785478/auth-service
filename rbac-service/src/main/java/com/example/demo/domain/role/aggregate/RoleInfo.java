package com.example.demo.domain.role.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.domain.role.aggregate.entity.RoleFunction;
import com.example.demo.domain.role.aggregate.vo.RoleProfile;
import com.example.demo.domain.role.aggregate.vo.RoleScope;
import com.example.demo.shared.enums.YesNo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
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
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
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
@AllArgsConstructor
@Table(name = "role_info", uniqueConstraints = {
		@UniqueConstraint(name = "uk_role_service_code", columnNames = { "service", "code" }) })
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private RoleScope scope; // VO: 取代原本的 service, code

	@Embedded
	private RoleProfile profile; // VO: 取代原本的 name, description

	private String type; // 權限種類

	// 使用懶加載，避免 N+1 query 效能問題
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	private List<RoleFunction> functions = new ArrayList<>(); // 角色所屬功能

	@Enumerated(EnumType.STRING)
	private YesNo activeFlag = YesNo.Y; // 是否有效

	/**
	 * 工廠方法: 新增一筆角色資料
	 * 
	 * @param scope   角色範圍
	 * @param profile 角色描述
	 * @param type    角色種類
	 * @return 角色資料
	 */
	public static RoleInfo create(RoleScope scope, RoleProfile profile, String type) {
		RoleInfo role = new RoleInfo();
		role.scope = scope;
		role.profile = profile;
		role.type = type;
		role.activeFlag = YesNo.Y;
		return role;
	}

//	/**
//	 * 新增/更新一筆角色資料
//	 * 
//	 * @param command {@link CreateOrUpdateRoleCommand}
//	 */
//	public static RoleInfo create(CreateOrUpdateRoleCommand command) {
//		RoleInfo roleInfo = new RoleInfo();
//		roleInfo.service = command.getService();
//		roleInfo.code = command.getCode();
//		roleInfo.name = command.getName();
//		roleInfo.description = command.getDescription();
//		roleInfo.type = command.getType();
//		roleInfo.activeFlag = YesNo.Y;
//		return roleInfo;
//	}
//	
//	/**
//	 * 更新一筆角色資料
//	 * 
//	 * @param command
//	 */
//	public void update(CreateOrUpdateRoleCommand command) {
//		this.id = command.getId();
//		this.service = command.getService();
//		this.code = command.getCode();
//		this.name = command.getName();
//		this.type = command.getType();
//		this.description = command.getDescription();
//		this.activeFlag = YesNo.valueOf(command.getActiveFlag());
//	}

	/**
	 * 領域行為：更新角色基本資料 (意圖明確)
	 */
	public void updateProfile(RoleProfile newProfile, String newType) {
		this.profile = newProfile;
		this.type = newType;
	}

	/**
	 * 領域行為：修改業務範圍 (如果業務允許修改的話)
	 */
	public void changeScope(RoleScope newScope) {
		this.scope = newScope;
	}

	/**
	 * 更新一筆角色資料
	 * 
	 * @param scope   角色範圍
	 * @param profile 角色描述
	 * @param type    角色種類
	 */
	public void update(RoleScope scope, RoleProfile profile, String type) {
		this.type = type;
		this.scope = scope;
		this.profile = profile;
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
	}

	/**
	 * 刪除角色功能資料 (ActiveFlag = "N")
	 */
	public void delete() {
		this.activeFlag = YesNo.N;
	}

}
