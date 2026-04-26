package com.example.demo.iface.dto.response.response;

import java.util.List;

import com.example.demo.application.shared.dto.FieldViewCustomisationQueried;

public record FieldViewCustomisationGottenResource(String code, String message,
		List<FieldViewCustomisationQueried> data) {

}
