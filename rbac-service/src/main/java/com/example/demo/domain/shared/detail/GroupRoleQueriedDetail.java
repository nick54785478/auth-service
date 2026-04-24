package com.example.demo.domain.shared.detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRoleQueriedDetail {

	private Long id; // 角色 ID
	
	private String service; // 服務

	private String code; // 角色代號

	private String name; // 名稱

	private String description; // 敘述
}
