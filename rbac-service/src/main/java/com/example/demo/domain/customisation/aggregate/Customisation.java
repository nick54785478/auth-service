package com.example.demo.domain.customisation.aggregate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.domain.customisation.command.UpdateCustomisationCommand;
import com.example.demo.shared.enums.YesNo;
import com.example.demo.util.JsonParseUtil;

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
@Table(name = "customisation")
@EntityListeners(AuditingEntityListener.class)
public class Customisation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username")
	private String username; // 帳號

	@Column(name = "component")
	private String component; // Component 名稱

	@Column(name = "type")
	private String type; // 種類，TABLE_FIELD

	@Column(name = "name")
	private String name;

	@Column(name = "value")
	private String value; 

	@Enumerated(EnumType.STRING)
	@Column(name = "active_flag")
	private YesNo activeFlag; // 是否生效

	/**
	 * 更新個人化設定
	 * 
	 * @param command
	 */
	public void update(UpdateCustomisationCommand command) {
		this.username = command.getUsername();
		this.component = command.getComponent();
		this.type = command.getType();
		this.value = JsonParseUtil.serialize(command.getValueList());
		this.activeFlag = YesNo.Y;

	}
}
