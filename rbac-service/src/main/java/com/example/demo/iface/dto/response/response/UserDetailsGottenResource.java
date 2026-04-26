package com.example.demo.iface.dto.response.response;

import com.example.demo.domain.shared.summary.UserInfoDetailsQueriedSummary;

public record UserDetailsGottenResource(String code, String message, UserInfoDetailsQueriedSummary data) {

}
