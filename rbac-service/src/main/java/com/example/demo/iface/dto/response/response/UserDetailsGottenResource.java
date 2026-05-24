package com.example.demo.iface.dto.response.response;

import com.example.demo.application.shared.dto.UserDetailsQueried;

public record UserDetailsGottenResource(String code, String message, UserDetailsQueried data) {

}
