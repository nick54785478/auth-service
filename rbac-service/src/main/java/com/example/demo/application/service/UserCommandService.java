package com.example.demo.application.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.service.UserService;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.domain.user.command.CreateUserCommand;
import com.example.demo.domain.user.command.UpdateUserCommand;
import com.example.demo.domain.user.command.UpdateUserRolesCommand;
import com.example.demo.infra.exception.ValidationException;
import com.example.demo.infra.repository.UserInfoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class UserCommandService {

	private UserService userService;
	private UserInfoRepository userRepository;

	/**
	 * 建立使用者資料
	 * 
	 * @param command
	 */
	public void create(CreateUserCommand command) {
		if (!userService.checkIsRegistered(command)) {
			throw new ValidationException("VALIDATION_FAILED", "該使用者相關資訊已註冊");
		}
		UserInfo userInfo = new UserInfo();
		userInfo.create(command);
		userRepository.save(userInfo);
	}

	/**
	 * 更新使用者資料
	 * 
	 * @param command
	 */
	public void update(UpdateUserCommand command) {
		Optional<UserInfo> opt = userRepository.findById(command.getId());
		if (opt.isPresent()) {
			var userInfo = opt.get();
			userInfo.update(command);
			userRepository.save(userInfo);
		} else {
			throw new ValidationException("VALIDATION_FAILED", "查無此資料 id，更新失敗");
		}
	}

	/**
	 * 更新 使用者角色資料
	 * 
	 * @param command
	 */
	public void grant(UpdateUserRolesCommand command) {
		userService.grant(command);
	}
}
