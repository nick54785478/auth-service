package com.example.demo.domain.shared.detail;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupsAuthQueriedDetail {

	private String username;
	
	private String email;

	private List<String> roles;

	private List<String> functions;
}
