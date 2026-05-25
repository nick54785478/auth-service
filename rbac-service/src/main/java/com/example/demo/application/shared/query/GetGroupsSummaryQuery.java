package com.example.demo.application.shared.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetGroupsSummaryQuery {

	private String service;

	private String type;

	private String name;

	private String activeFlag;
}
