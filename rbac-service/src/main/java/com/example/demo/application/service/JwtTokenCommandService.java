package com.example.demo.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.example.demo.application.port.JwTokenManagerPort;
import com.example.demo.application.shared.dto.JwtTokenGenerated;
import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.service.RoleFunctionService;
import com.example.demo.domain.service.UserGroupService;
import com.example.demo.domain.service.UserRoleService;
import com.example.demo.domain.service.UserService;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.domain.user.command.GenerateJwtokenCommand;
import com.example.demo.domain.user.command.RefreshTokenCommand;
import com.example.demo.infra.context.ContextHolder;
import com.example.demo.infra.exception.ValidationException;
import com.example.demo.infra.repository.UserInfoRepository;
import com.example.demo.util.PasswordUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 用於執行 Jwt 相關操作的 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenCommandService {

	private final UserService userService;
	private final UserRoleService userRoleService;
	private final JwTokenManagerPort jwTokenManager;
	private final UserGroupService userGroupService;
	private final UserInfoRepository userInfoRepository;
	private final RoleFunctionService roleFunctionService;

	/**
	 * 建立 JWToken
	 * 
	 * @param command {@link GenerateJwtokenCommand}
	 * @return token
	 */
	@Transactional
	public JwtTokenGenerated generateToken(GenerateJwtokenCommand command) {
		// 透過 ContextHolder 取得 Service
		String service = ContextHolder.getService();

		UserInfo userInfo = userInfoRepository.findByUsername(command.getUsername());

		if (Objects.isNull(userInfo)) {
			throw new ValidationException("VALIDATION_FAILED", "該使用者帳號不存在");// 比對失敗
		}

		boolean checkPassword = PasswordUtil.checkPassword(command.getPassword(), userInfo.getPassword());
		log.info("command: username:{}, password:{}", command.getUsername(), command.getPassword());

		// 檢查密碼是否相符
		if (!checkPassword) {
			throw new ValidationException("VALIDATION_FAILED", "使用者帳號或密碼有誤");// 比對失敗
		}

		// 查詢該使用者所在的群組
		List<GroupInfo> queryGroups = userGroupService.queryGroups(command.getUsername(), ContextHolder.getService());
		List<String> groups = queryGroups.stream().map(GroupInfo::getCode).collect(Collectors.toList());

		// 查詢該使用者個人角色
		List<RoleInfo> queryRoles = userRoleService.queryRoles(command.getUsername(), ContextHolder.getService());
		List<String> roles = queryRoles.stream().map(RoleInfo::getCode).collect(Collectors.toList());

		// 取得該角色清單所具備的相關功能權限
		Set<String> functionCodes = roleFunctionService.getFunctionsByRoleIds(service, roles).getFuncList().stream()
				.map(FunctionInfo::getCode).collect(Collectors.toSet());
		JwtTokenGenerated tokenGenerated = jwTokenManager.generateToken(userInfo.getUsername(), userInfo.getEmail(),
				roles, groups, new ArrayList<>(functionCodes));

		// 更新 Refresh Token
		userInfo.updateRefreshToken(tokenGenerated.getRefreshToken());
		userInfoRepository.save(userInfo);

		// 若不存在 RefreshToken，設置進去
		if (StringUtils.isBlank(userInfo.getRefreshToken())) {
			userInfo.updateRefreshToken(tokenGenerated.getRefreshToken());
		}

		// 如果 RefreshToken 過期
		if (jwTokenManager.isExpiration(userInfo.getRefreshToken())) {
			userInfo.updateRefreshToken(tokenGenerated.getRefreshToken());
		}
		return tokenGenerated;
	}

	/**
	 * 刷新 Token
	 * 
	 * @param command
	 * @return JwtokenGenerated
	 */
	public JwtTokenGenerated refresh(RefreshTokenCommand command) {
		UserInfo userInfo = userInfoRepository.findByRefreshToken(command.getToken());
		if (!Objects.isNull(userInfo)) {
			// 查詢該使用者群組資訊
			List<GroupInfo> queryGroups = userService.queryGroups(userInfo.getUsername());
			List<String> groups = queryGroups.stream().map(GroupInfo::getCode).collect(Collectors.toList());

			// 查詢該使用者相關角色資訊
			List<RoleInfo> queryRoles = userService.queryRoles(userInfo.getUsername());
			List<String> roles = queryRoles.stream().map(RoleInfo::getCode).collect(Collectors.toList());

			// 取出相關 Service
			String service = ContextHolder.getService();

			// 取得該角色清單所具備的相關功能權限
			Set<String> functions = roleFunctionService.getFunctionsByRoleIds(service, roles).getFuncList().stream()
					.map(FunctionInfo::getCode).collect(Collectors.toSet());
			JwtTokenGenerated tokenGenerated = jwTokenManager.generateToken(userInfo.getUsername(), userInfo.getEmail(),
					roles, groups, new ArrayList<>(functions));

			// 若不存在 RefreshToken，設置進去
			if (StringUtils.isBlank(userInfo.getRefreshToken())) {
				userInfo.updateRefreshToken(tokenGenerated.getRefreshToken());
			} else {

				// 若 Refresh Token 過期，更新該 Refresh Token
				if (jwTokenManager.isExpiration(userInfo.getRefreshToken())) {
					userInfo.updateRefreshToken(tokenGenerated.getRefreshToken());
				} else {
					// Refresh Token 未過期，沿用舊 Token
					tokenGenerated.setRefreshToken(userInfo.getRefreshToken());
				}
			}
			userInfoRepository.save(userInfo);
			return tokenGenerated;
		}
		return null;
	}

}
