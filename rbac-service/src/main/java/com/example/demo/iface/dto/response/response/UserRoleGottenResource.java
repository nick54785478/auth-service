package com.example.demo.iface.dto.response.response;

import java.util.List;

import com.example.demo.application.shared.dto.UserRoleQueried;

public record UserRoleGottenResource(String code, String message, List<UserRoleQueried> data) {

}
