package com.example.demo.iface.dto.response.response;

import java.util.List;

import com.example.demo.application.shared.dto.RoleOptionQueried;

public record RoleOptionsGottenResource(String code, String message, List<RoleOptionQueried> data) {
}
