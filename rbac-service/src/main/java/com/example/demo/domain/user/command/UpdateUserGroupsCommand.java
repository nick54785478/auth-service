package com.example.demo.domain.user.command;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserGroupsCommand {

	private String username; // 使用者帳號

	private String service; // 服務

	private List<Long> groupIds; // 群組 ID 清單
}
