package com.example.demo.domain.role.aggregate.entity;

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
@Table(name = "role_function")
@EntityListeners(AuditingEntityListener.class)
public class RoleFunction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "role_id")
	private Long roleId;
	
	@Column(name = "function_id")
	private Long functionId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "active_flag")
	private YesNo activeFlag;
	
	/**
	 * 建立一筆權限資料
	 * 
	 * @param roleId
	 * @param functionId
	 * */
	public void create(Long roleId, Long functionId) {
		this.roleId = roleId;
		this.functionId = functionId;
		this.activeFlag = YesNo.Y;
	}
	
	/**
	 * 更新一筆角色功能資料
	 * 
	 * @param id
	 * @param roleId
	 * @param functionId
	 */
	public void update(Long id,  Long roleId, Long functionId) {
		this.id = id;
		this.roleId = roleId;
		this.functionId = functionId;
	}
	
	/**
	 * 刪除動作
	 * */
	public void delete() {
		this.activeFlag = YesNo.N;
	}
}
