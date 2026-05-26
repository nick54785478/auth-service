package com.example.demo.application.shared.query;

import com.example.demo.infra.exception.ValidationException;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetFunctionsSummaryQuery {

	private String service;

	private String type;

	private String actionType;

	private String name;

	private String activeFlag;

	public GetFunctionsSummaryQuery(String service, String type, String actionType, String name, String activeFlag) {

		if (service == null) {
			throw new ValidationException("NOT_BLANK", "Service 不能為空");
		}

		this.service = service;
		this.type = type;
		this.actionType = actionType;
		this.name = name;
		this.activeFlag = activeFlag;
	}

}
