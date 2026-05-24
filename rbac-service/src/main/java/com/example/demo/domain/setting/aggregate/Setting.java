package com.example.demo.domain.setting.aggregate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.domain.setting.aggregate.vo.SettingProfile;
import com.example.demo.domain.setting.aggregate.vo.SettingScope;
import com.example.demo.shared.enums.YesNo;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@Table(name = "setting")
@EntityListeners(AuditingEntityListener.class)
public class Setting {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "data_type")
	private String dataType; // 資料類型

	@Column(name = "type")
	private String type; // 種類

	@Column(name = "value")
	private String value; // 值

	@Embedded
	private SettingScope scope; // 替換 service, code

	@Embedded
	private SettingProfile profile; // 替換 name, description

	@Column(name = "priority_no")
	private Integer priorityNo; // 順序號(從 1 開始)

	@Enumerated(EnumType.STRING)
	@Column(name = "active_flag")
	private YesNo activeFlag = YesNo.Y; // 是否有效

	/**
	 * 純粹的工廠方法
	 * 
	 * @param profile    設定範圍
	 * @param dataType   資料種類
	 * @param type       種類
	 * @param value      值
	 * @param priorityNo 順序
	 */
	public static Setting create(SettingScope scope, SettingProfile profile, String dataType, String type, String value,
			Integer priorityNo) {
		Setting setting = new Setting();
		setting.scope = scope;
		setting.profile = profile;
		setting.dataType = dataType;
		setting.type = type;
		setting.value = value;
		setting.priorityNo = priorityNo;
		setting.activeFlag = YesNo.Y;
		return setting;
	}

	/**
	 * 修改一筆 Setting
	 * 
	 * @param profile    設定範圍
	 * @param dataType   資料種類
	 * @param type       種類
	 * @param value      值
	 * @param priorityNo 順序
	 * @param activeFlag 是否生效
	 */
	public void update(SettingProfile profile, String dataType, String type, String value, Integer priorityNo,
			String activeFlag) {
		this.profile = profile;
		this.dataType = dataType;
		this.type = type;
		this.value = value;
		this.priorityNo = priorityNo;
		this.activeFlag = YesNo.valueOf(activeFlag);
	}

	/**
	 * 刪除 (更改 activeFlag = 'N')
	 */
	public void delete() {
		this.activeFlag = YesNo.N;
	}
}
