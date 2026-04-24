package com.example.demo.iface.dto.out;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalAuthQueriedResource {
	
	private String username;

	private String email;
	
	private List<String> roles;

	private List<String> functions;
}
