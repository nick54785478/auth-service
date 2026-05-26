package com.example.demo.domain.user.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.domain.user.aggregate.UserInfo;

public interface UserInfoRepository {

	Optional<UserInfo> findById(Long id);

	UserInfo save(UserInfo user);

	List<UserInfo> saveAll(List<UserInfo> users);

	List<UserInfo> findByIdIn(List<Long> ids);

	UserInfo findByUsername(String username);

	List<UserInfo> findByUsernameOrProfileNationalIdNoOrProfileEmail(String username, String nationalIdNo,
			String email);

	UserInfo findByRefreshToken(String refreshToken);

	List<UserInfo> findByUsernameContaining(String username);

}
