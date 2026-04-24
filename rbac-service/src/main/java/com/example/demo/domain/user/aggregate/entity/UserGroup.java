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
@Table(name = "user_group")
@EntityListeners(AuditingEntityListener.class)
public class UserGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "group_id")
	private Long groupId;

	@Enumerated(EnumType.STRING)
	@Column(name = "active_flag")
	private YesNo activeFlag;

	/**
	 * 建立使用者所在群組資訊
	 * 
	 * @param groupId 群組 ID
	 * @param userId  使用者 ID
	 */
	public void create(Long groupId, Long userId) {
		this.userId = userId;
		this.groupId = groupId;
		this.activeFlag = YesNo.Y;
	}
	
	/**
	 * 更新一筆使用者群組資料
	 * 
	 * @param id
	 * @param userId
	 * @param roleId
	 */
	public void update(Long id, Long userId, Long groupId) {
		this.id = id;
		this.userId = userId;
		this.groupId = groupId;
	}
	
	/**
	 * 刪除動作
	 * */
	public void delete() {
		this.activeFlag = YesNo.N;
	}

	
}
