package com.example.demo.domain.shared.detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleFunctionQueriedDetail {

	private Long id;

	private String type;

	private String code;

	private String name;

	private String actionType;

	private String description;

	private String activeFlag;

}
