package com.example.demo.iface.dto.response.response;

import java.util.List;

public record TokenValidatedAndParsedResource(String username, List<String> roles, List<String> groups,
		List<String> functions) {
}
