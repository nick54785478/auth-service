package com.example.demo.application.shared.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRoleByServiceAndKeywordQuery {

	private String service;
	
	private String keyword;
}
