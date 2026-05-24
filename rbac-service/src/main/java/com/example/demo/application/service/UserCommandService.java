package com.example.demo.application.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.shared.command.CreateUserCommand;
import com.example.demo.application.shared.command.UpdateUserCommand;
import com.example.demo.application.shared.command.UpdateUserRolesCommand;
import com.example.demo.domain.service.UserService;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.domain.user.aggregate.vo.UserProfile;
import com.example.demo.infra.exception.ValidationException;
import com.example.demo.infra.repository.UserInfoRepository;
import com.example.demo.util.PasswordUtil;

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
	 * @param command {@link CreateUserCommand}
	 */
	public void create(CreateUserCommand command) {
		if (!userService.checkIsRegistered(command.getUsername(), command.getEmail(), command.getNationalId())) {
			throw new ValidationException("VALIDATE_FAILED", "該使用者相關資訊已註冊");
		}
		String encodedPassword = PasswordUtil.encode(command.getPassword());
		UserProfile userProfile = UserProfile.of(command.getName(), command.getEmail(), command.getNationalId(),
				command.getBirthday(), command.getAddress());
		UserInfo userInfo = UserInfo.create(command.getUsername(), encodedPassword, userProfile);
		userRepository.save(userInfo);
	}

	/**
	 * 更新使用者資料
	 * 
	 * @param id      使用者 ID
	 * @param command {@link UpdateUserCommand}
	 */
	public void update(Long id, UpdateUserCommand command) {
		Optional<UserInfo> opt = userRepository.findById(id);
		if (opt.isPresent()) {
			var userInfo = opt.get();
			UserProfile userProfile = UserProfile.of(command.getName(), command.getEmail(), command.getNationalId(),
					command.getBirthday(), command.getAddress());
			userInfo.updateProfile(userProfile);
			userRepository.save(userInfo);
		} else {
			throw new ValidationException("VALIDATE_FAILED", "查無此資料 id，更新失敗");
		}
	}

	/**
	 * 更新使用者角色資料
	 * 
	 * @param command {@link UpdateUserRolesCommand}
	 */
	public void grant(UpdateUserRolesCommand command) {
		userService.grant(command.getUsername(), command.getRoleIds());
	}
}
