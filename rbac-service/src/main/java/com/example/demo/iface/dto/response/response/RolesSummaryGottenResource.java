package com.example.demo.iface.dto.response.response;

import java.util.List;

import com.example.demo.application.shared.dto.RoleInfoQueried;

public record RolesSummaryGottenResource(String code, String message, List<RoleInfoQueried> data) {

}
