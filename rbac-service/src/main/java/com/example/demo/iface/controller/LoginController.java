package com.example.demo.iface.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.service.JwtTokenCommandService;
import com.example.demo.domain.user.command.GenerateJwtokenCommand;
import com.example.demo.domain.user.command.RefreshTokenCommand;
import com.example.demo.iface.dto.in.GenerateJwtokenResource;
import com.example.demo.iface.dto.in.RefreshTokenResource;
import com.example.demo.iface.dto.out.JwtTokenGeneratedResource;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class LoginController {

	private JwtTokenCommandService commandService;

	/**
	 * 登入並取得 Jwt Token
	 * 
	 * @param resource
	 * @return ResponseEntity<JwtokenGeneratedResource>
	 */
	@PostMapping("/login")
	public ResponseEntity<JwtTokenGeneratedResource> login(@RequestBody GenerateJwtokenResource resource) {
		GenerateJwtokenCommand command = BaseDataTransformer.transformData(resource, GenerateJwtokenCommand.class);
		return new ResponseEntity<>(BaseDataTransformer.transformData(commandService.generateToken(command),
				JwtTokenGeneratedResource.class), HttpStatus.OK);
	}

	/**
	 * 刷新 Jwt Token
	 * 
	 * @param resource
	 * @return ResponseEntity<JwtokenGeneratedResource>
	 */
	@PostMapping("/refresh")
	public ResponseEntity<JwtTokenGeneratedResource> refresh(@RequestBody RefreshTokenResource resource) {

		RefreshTokenCommand command = BaseDataTransformer.transformData(resource, RefreshTokenCommand.class);
		return new ResponseEntity<>(
				BaseDataTransformer.transformData(commandService.refresh(command), JwtTokenGeneratedResource.class),
				HttpStatus.OK);
	}
}
