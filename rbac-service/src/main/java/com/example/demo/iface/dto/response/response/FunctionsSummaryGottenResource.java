package com.example.demo.iface.dto.response.response;

import java.util.List;

import com.example.demo.application.shared.dto.FunctionInfoQueried;

public record FunctionsSummaryGottenResource(String code, String message, List<FunctionInfoQueried> data) {
}
