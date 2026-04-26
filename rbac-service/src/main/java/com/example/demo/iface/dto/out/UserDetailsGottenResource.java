package com.example.demo.iface.dto.out;

import com.example.demo.domain.shared.summary.UserInfoDetailsQueriedSummary;

public record UserDetailsGottenResource(String code, String message, UserInfoDetailsQueriedSummary data) {

}
