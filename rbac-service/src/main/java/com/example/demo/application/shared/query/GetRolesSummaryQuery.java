package com.example.demo.application.shared.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRolesSummaryQuery {

	private String service;

	private String type;

	private String name;

	private String activeFlag;
	
}
