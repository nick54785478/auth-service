package com.example.demo.iface.dto.response.response;

import java.util.List;

import com.example.demo.application.shared.dto.GroupOptionQueried;

public record GroupOptionsGottenResource(String code, String message, List<GroupOptionQueried> data) {
}
