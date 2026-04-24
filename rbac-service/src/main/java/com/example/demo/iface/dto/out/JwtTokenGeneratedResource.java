package com.example.demo.iface.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenGeneratedResource {

	private String token;
	
	private String refreshToken;

}
