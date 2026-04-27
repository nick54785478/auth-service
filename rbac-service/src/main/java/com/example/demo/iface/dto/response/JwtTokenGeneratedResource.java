package com.example.demo.iface.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenGeneratedResource {

	private String code;

	private String message;

	private String token;

	private String refreshToken;

}
