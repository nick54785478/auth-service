package com.example.demo.application.shared.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidatedAndParsed {

	private String username;

	private List<String> roles;

	private List<String> groups;

	private List<String> functions;
}
