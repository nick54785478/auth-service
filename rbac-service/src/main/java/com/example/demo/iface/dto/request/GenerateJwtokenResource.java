package com.example.demo.iface.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateJwtokenResource {

	private String username;
	
	private String password;
}
