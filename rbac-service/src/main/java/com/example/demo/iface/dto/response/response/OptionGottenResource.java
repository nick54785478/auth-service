package com.example.demo.iface.dto.response.response;

import java.util.List;

import com.example.demo.application.shared.dto.OptionQueried;

public record OptionGottenResource(String code, String message, List<OptionQueried> data) {

}
