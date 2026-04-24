package com.example.demo.domain.user.aggregate.entity;

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
@Table(name = "user_role")
@EntityListeners(AuditingEntityListener.class)
public class UserRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "role_id")
	private Long roleId;

	@Enumerated(EnumType.STRING)
	@Column(name = "active_flag")
	private YesNo activeFlag;

	/**
	 * 建立一筆使用者角色資料
	 * 
	 * @param userId
	 * @param roleId
	 */
	public void create(Long userId, Long roleId) {
		this.userId = userId;
		this.roleId = roleId;
		this.activeFlag = YesNo.Y;
	}
	
	/**
	 * 更新一筆使用者角色資料
	 * 
	 * @param id
	 * @param userId
	 * @param roleId
	 */
	public void update(Long id, Long userId, Long roleId) {
		this.id = id;
		this.userId = userId;
		this.roleId = roleId;
	}
	
	/**
	 * 刪除動作
	 * */
	public void delete() {
		this.activeFlag = YesNo.N;
	}

}
