package com.example.demo.iface.dto.response.response;

import com.example.demo.application.shared.dto.UserInfoQueried;

public record UserInfoGottenResource(String code, String message, UserInfoQueried data) {

}
