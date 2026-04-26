package com.example.demo.iface.dto.response.response;

import java.util.List;

import com.example.demo.application.shared.dto.RoleFunctionQueried;

public record RoleFunctionsGottenResource(String code, String message, List<RoleFunctionQueried> data) {

}
