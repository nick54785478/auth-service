package com.example.demo.application.shared.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetSettingSummaryQuery {
	private String service;
	private String dataType;
	private String type;
	private String name;
	private String activeFlag;
}
