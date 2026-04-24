package com.example.demo.domain.shared.summary;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.domain.shared.detail.GroupRoleQueriedDetail;
import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfoQueriedSummary {

	private Long id;

	private String service;

	private String type; // 配置種類

	private String code; // Code

	private String name;

	@Default
	private List<GroupRoleQueriedDetail> roles = new ArrayList<>();

	private String description; // 敘述

	@Default
	private YesNo activeFlag = YesNo.Y; // 是否有效
}
