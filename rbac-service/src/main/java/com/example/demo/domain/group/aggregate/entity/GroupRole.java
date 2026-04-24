package com.example.demo.domain.group.aggregate.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "group_role")
@EntityListeners(AuditingEntityListener.class)
public class GroupRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "role_id")
	private Long roleId;

	@Column(name = "group_id")
	private Long groupId;

	@Enumerated(EnumType.STRING)
	private YesNo activeFlag = YesNo.Y; // 是否有效

	/**
	 * 新增一筆群組角色資料
	 * 
	 * @param groupId
	 * @param roleId
	 */
	public void create(Long groupId, Long roleId) {
		this.groupId = groupId;
		this.roleId = roleId;
		this.activeFlag = YesNo.Y;
	}

	/**
	 * 更新一筆角色功能資料
	 * 
	 * @param id
	 * @param groupId
	 * @param roleId
	 */
	public void update(Long id, Long groupId, Long roleId) {
		this.id = id;
		this.groupId = groupId;
		this.roleId = roleId;
	}

	/**
	 * 變更狀態
	 * 
	 * @param activeFlag
	 */
	public void updateStatus(YesNo activeFlag) {
		this.activeFlag = activeFlag;
	}

	public GroupRole(Long roleId, Long groupId, YesNo activeFlag) {
		this.roleId = roleId;
		this.groupId = groupId;
		this.activeFlag = activeFlag;
	}

	/**
	 * 刪除角色資料 (ActiveFlag = "N")
	 */
	public void delete() {
		this.activeFlag = YesNo.N;
	}
}
