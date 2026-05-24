package com.example.demo.application.shared.dto;

import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleFunctionQueried {

	private Long id;

	private String type;

	private String code;

	private String name;

	private String actionType;

	private String description;

	private YesNo activeFlag;

}
