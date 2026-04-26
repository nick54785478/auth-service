package com.example.demo.iface.dto.out;

import java.util.List;

import com.example.demo.application.shared.dto.GroupInfoQueried;

public record GroupsSummaryGottenResource(String code, String message, List<GroupInfoQueried> data) {
}
