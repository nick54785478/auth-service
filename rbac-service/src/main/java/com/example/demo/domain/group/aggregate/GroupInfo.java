package com.example.demo.domain.group.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.domain.group.aggregate.entity.GroupRole;
import com.example.demo.domain.group.aggregate.vo.GroupProfile;
import com.example.demo.domain.group.aggregate.vo.GroupScope;
import com.example.demo.shared.enums.YesNo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
 * 群組表
 */
@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 隱藏預設建構子，保護 Aggregate
@Table(name = "group_info", uniqueConstraints = {
		@UniqueConstraint(name = "uk_group_service_code", columnNames = { "service", "code" }) })
@EntityListeners(AuditingEntityListener.class)
public class GroupInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private GroupScope scope; // 替換原本的 service, code

	@Embedded
	private GroupProfile profile; // 替換原本的 name, description

	@Column(name = "type")
	private String type; // 群組種類

	// 使用懶加載，避免 N+1 query 效能問題
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id")
	private List<GroupRole> roles = new ArrayList<>(); // 群組所屬角色

	@Enumerated(EnumType.STRING)
	private YesNo activeFlag = YesNo.Y; // 是否有效

	/**
	 * 工廠方法: 新增一筆群組資料
	 * 
	 * @param scope   群組範圍
	 * @param profile 群組描述
	 * @param type    群組種類
	 * @return 群組資料
	 */
	public static GroupInfo create(GroupScope scope, GroupProfile profile, String type) {
		GroupInfo group = new GroupInfo();
		group.scope = scope;
		group.profile = profile;
		group.type = type;
		group.activeFlag = YesNo.Y;
		return group;
	}

	/**
	 * 更新一筆群組資料
	 * 
	 * @param scope      群組範圍
	 * @param profile    群組描述
	 * @param type       群組種類
	 * @param activeFlag 是否生效
	 */
	public void update(GroupScope scope, GroupProfile profile, String type, String activeFlag) {
		this.scope = scope;
		this.profile = profile;
		this.type = type;
		this.activeFlag = YesNo.valueOf(activeFlag);
	}

	/**
	 * 更新群組角色
	 * 
	 * @param roleIds 欲變更的群組角色 ID
	 */
	public void updateRoles(List<GroupRole> groupRoles) {
		// DB 內的角色 ID Map
		Map<Long, GroupRole> existMap = this.roles.stream()
				.collect(Collectors.toMap(GroupRole::getRoleId, Function.identity()));

		// 新資料沒有但舊資料有 => 刪除
		List<GroupRole> result = this.roles.stream().filter(existingRole -> groupRoles.stream()
				.noneMatch(newRole -> newRole.getRoleId().equals(existingRole.getRoleId()))).peek(role -> {
					role.delete();
				}) // peek 在收集到清單之前執行
				.collect(Collectors.toList());
		// 遍歷使用者的角色資料蒐集
		groupRoles.stream().forEach(e -> {
			// functionId 對不到 --> 新資料中有但舊資料沒有的資料 => 新增
			if (Objects.isNull(existMap.get(e.getRoleId()))) {
				result.add(e);
			} else {
				// 有對到 --> 新蓋舊
				GroupRole old = existMap.get(e.getRoleId());
				e.update(old.getId(), old.getGroupId(), old.getRoleId());
				result.add(e);
			}
		});
		this.roles = result;
	}

	/**
	 * 刪除角色資料 (ActiveFlag = "N")
	 */
	public void delete() {
		this.activeFlag = YesNo.N;
	}

}
