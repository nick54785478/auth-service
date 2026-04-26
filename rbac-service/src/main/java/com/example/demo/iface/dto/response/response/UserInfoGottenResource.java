package com.example.demo.iface.dto.response.response;

import com.example.demo.domain.shared.summary.UserInfoQueriedSummary;

public record UserInfoGottenResource(String code, String message, UserInfoQueriedSummary data) {

}
