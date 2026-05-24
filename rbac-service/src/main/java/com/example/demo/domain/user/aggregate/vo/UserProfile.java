package com.example.demo.domain.user.aggregate.vo;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 使用者個人檔案 (VO)
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {

	@Column(name = "name")
	private String name; // 使用者姓名

	@Column(name = "email")
	private String email; // 信箱

	@Column(name = "national_id")
	private String nationalIdNo; // 身分證字號

	@Column(name = "birthday")
	private Date birthday; // 出生年月日

	@Column(name = "address")
	private String address; // 地址

	/**
	 * 工廠類方法 : 填充 UserProfile
	 * 
	 * @param name         使用者名稱
	 * @param email        信箱
	 * @param nationalIdNo 身分證字號
	 * @param birthday     出生年月日
	 * @param address      地址
	 * @return {@link UserProfile}
	 */
	public static UserProfile of(String name, String email, String nationalIdNo, Date birthday, String address) {
		UserProfile profile = new UserProfile();
		profile.name = name;
		profile.email = email;
		profile.nationalIdNo = nationalIdNo;
		profile.birthday = birthday;
		profile.address = address;
		return profile;
	}
}