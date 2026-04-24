package com.example.demo.domain.user.aggregate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.domain.user.aggregate.entity.UserGroup;
import com.example.demo.domain.user.aggregate.entity.UserRole;
import com.example.demo.domain.user.command.CreateUserCommand;
import com.example.demo.domain.user.command.UpdateUserCommand;
import com.example.demo.shared.enums.YesNo;
import com.example.demo.util.PasswordUtil;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")
@EntityListeners(AuditingEntityListener.class)
public class UserInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name; // 使用者姓名

	private String email; // 信箱

	private String username; // 帳號

	private String password; // 密碼

	@Column(name = "national_id")
	private String nationalIdNo; // 身分證字號

	private Date birthday; // 出生年月日

	private String address;

	@Column(name = "refresh_token")
	private String refreshToken;

	// 使用懶加載，避免 N+1 query 效能問題
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private List<UserRole> roles = new ArrayList<>(); // 角色清單

	// 使用懶加載，避免 N+1 query 效能問題
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private List<UserGroup> groups = new ArrayList<>(); // 角色所屬群組

	@Enumerated(EnumType.STRING)
	private YesNo activeFlag = YesNo.Y; // 是否有效

	/**
	 * Constructor
	 */
	public UserInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * 設置 Refresh Token
	 */
	public void updateRefreshToken(String token) {
		this.refreshToken = token;
	}

	/**
	 * 新增使用者資料
	 * 
	 * @param command
	 */
	public void create(CreateUserCommand command) {
		this.name = command.getName();
		this.username = command.getUsername();
		this.password = PasswordUtil.encode(command.getPassword());
		this.birthday = command.getBirthday();
		this.nationalIdNo = command.getNationalId();
		this.email = command.getEmail();
		this.address = command.getAddress();
		this.activeFlag = YesNo.Y;
	}

	/**
	 * 更新使用者資料
	 * 
	 * @param command
	 */
	public void update(UpdateUserCommand command) {
		this.birthday = command.getBirthday();
		this.nationalIdNo = command.getNationalId();
		this.name = (StringUtils.isNotBlank(command.getName())) ? command.getName() : this.name;
		this.email = (StringUtils.isNotBlank(command.getEmail())) ? command.getEmail() : this.email;
		this.address = (StringUtils.isNotBlank(command.getAddress())) ? command.getAddress() : this.address;
	}

	/**
	 * 更新 角色清單
	 * 
	 * @param userRoles 更新後的使用者角色清單
	 */
	public void updateRoles(List<UserRole> userRoles) {
		// DB 內的角色 ID Map
		Map<Long, UserRole> existMap = this.roles.stream()
				.collect(Collectors.toMap(UserRole::getRoleId, Function.identity()));

		// 新資料沒有但舊資料有 => 刪除
		List<UserRole> result = this.roles.stream()
				.filter(existingRole -> userRoles.stream()
						.noneMatch(newRole -> newRole.getRoleId().equals(existingRole.getRoleId())))
				.peek(UserRole::delete) // peek 在收集到清單之前執行
				.collect(Collectors.toList());
		// 遍歷使用者的角色資料蒐集
		userRoles.stream().forEach(e -> {
			// roleId 對不到 --> 新資料中有但舊資料沒有的資料 => 新增
			if (Objects.isNull(existMap.get(e.getRoleId()))) {
				result.add(e);
			} else {
				// 有對到 --> 新蓋舊
				UserRole old = existMap.get(e.getRoleId());
				e.update(old.getId(), old.getUserId(), old.getRoleId());
				result.add(e);
			}
		});
		this.roles = result;

	}

	/**
	 * 更新使用者群組
	 * 
	 * @param userGroups 使用者群組清單
	 */
	public void updateGroups(List<UserGroup> userGroups) {
		// DB 內的群組 ID Map
		Map<Long, UserGroup> existMap = this.groups.stream()
				.collect(Collectors.toMap(UserGroup::getGroupId, Function.identity()));
		// 新資料沒有但舊資料有 => 刪除
		List<UserGroup> result = this.groups.stream()
				.filter(existingGroup -> userGroups.stream()
						.noneMatch(newGroup -> newGroup.getGroupId().equals(existingGroup.getGroupId())))
				.peek(UserGroup::delete) // peek 在收集到清單之前執行
				.collect(Collectors.toList());

		// 遍歷使用者的群組資料蒐集
		userGroups.stream().forEach(e -> {
			// groupId 對不到 --> 新資料中有但舊資料沒有的資料 => 新增
			if (Objects.isNull(existMap.get(e.getGroupId()))) {
				result.add(e);
			} else {
				// 有對到 --> 新蓋舊
				UserGroup old = existMap.get(e.getGroupId());
				e.update(old.getId(), old.getUserId(), old.getGroupId());
				result.add(e);
			}
		});
		this.groups = result;
	}

	/**
	 * 刪除使用者資料 (ActiveFlag = "N")
	 * 
	 */
	public void delete() {
		this.activeFlag = YesNo.N;
	}

}
