package com.example.demo.application.shared.dto;

import java.util.Date;
import java.util.List;

import com.example.demo.domain.shared.detail.FunctionInfoDetailsQueriedDetail;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsQueried {

	private Long id;

	private String name;

	private String email; // 信箱

	private String username; // 帳號

	private String address;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
	private Date birthday;

	private String nationalIdNo;

	private String activeFlag;

	private List<GroupDetailQueried> groups;

	private List<RoleDetailQueried> roles;

	private List<FunctionInfoDetailsQueriedDetail> functions;
}
