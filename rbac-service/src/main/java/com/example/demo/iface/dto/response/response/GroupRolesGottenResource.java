package com.example.demo.iface.dto.response.response;

import java.util.List;

import com.example.demo.application.shared.dto.GroupRoleQueried;

public record GroupRolesGottenResource(String code, String message, List<GroupRoleQueried> data) {

}
