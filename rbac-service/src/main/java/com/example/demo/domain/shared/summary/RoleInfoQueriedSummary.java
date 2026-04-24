package com.example.demo.domain.shared.summary;

import java.util.List;

import com.example.demo.domain.shared.detail.RoleFunctionQueriedDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleInfoQueriedSummary {

	private Long id;

	private String service;

	private String code; // 角色 Code

	private String name;

	private String type; // 權限種類

	private String description; // 敘述

	private List<RoleFunctionQueriedDetail> functions; // 角色所屬功能

	private String activeFlag; // 是否有效
}
