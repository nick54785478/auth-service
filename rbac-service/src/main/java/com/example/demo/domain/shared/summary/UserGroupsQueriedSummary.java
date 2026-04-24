package com.example.demo.domain.shared.summary;

import java.util.List;

import com.example.demo.application.shared.dto.UserGroupQueried;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupsQueriedSummary {

	private Long id;

	private String name;

	private String email; // 信箱

	private String username; // 帳號

	private String address;

	private List<UserGroupQueried> groups;

}
