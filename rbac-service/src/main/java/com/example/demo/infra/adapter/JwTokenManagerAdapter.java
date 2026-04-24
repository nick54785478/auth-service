package com.example.demo.infra.adapter;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.application.port.JwTokenManagerPort;
import com.example.demo.application.shared.dto.JwtTokenGenerated;
import com.example.demo.infra.jwt.JwtTokenGenerator;
import com.example.demo.infra.jwt.JwtTokenParser;
import com.example.demo.infra.jwt.JwtTokenValidator;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
class JwTokenManagerAdapter implements JwTokenManagerPort {

	private JwtTokenGenerator jwTokenGenerator;
	private JwtTokenParser jwTokenParser;
	private JwtTokenValidator jwTokenValidator;

	@Override
	public JwtTokenGenerated generateToken(String username, String email, List<String> roles, List<String> groups,
			List<String> functions) {
		return jwTokenGenerator.generateToken(username, email, roles, groups, functions);
	}

	@Override
	public boolean validateToken(String token) {
		return jwTokenValidator.validateToken(token);
	}

	@Override
	public String getEmail(String token) {
		return jwTokenParser.getEmail(token);
	}

	@Override
	public List<String> getRoleList(String token) {
		return jwTokenParser.getRoleList(token);
	}

	@Override
	public String getUsername(String token) {
		return jwTokenParser.getUsername(token);
	}

	@Override
	public boolean isExpiration(String token) {
		return jwTokenParser.getTokenBody(token).getExpiration().before(new Date());
	}

	@Override
	public List<String> getGroupList(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getFuncList(String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
