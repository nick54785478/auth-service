package com.example.demo.iface.dto.response.response;

import java.util.List;

import com.example.demo.application.shared.dto.UserOptionQueried;

public record UserOptionGottenResource(String code, String message, List<UserOptionQueried> data) {

}
