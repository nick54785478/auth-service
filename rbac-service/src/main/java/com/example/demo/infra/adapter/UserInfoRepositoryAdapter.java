package com.example.demo.infra.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.domain.user.repository.UserInfoRepository;
import com.example.demo.infra.persistence.UserInfoPersistence;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
class UserInfoRepositoryAdapter implements UserInfoRepository {

	private UserInfoPersistence persistence;

	@Override
	public Optional<UserInfo> findById(Long id) {
		return persistence.findById(id);
	}

	@Override
	public UserInfo save(UserInfo user) {
		return persistence.save(user);
	}

	@Override
	public List<UserInfo> saveAll(List<UserInfo> users) {
		return persistence.saveAll(users);
	}

	@Override
	public List<UserInfo> findByIdIn(List<Long> ids) {
		return persistence.findByIdIn(ids);
	}

	@Override
	public UserInfo findByUsername(String username) {
		return persistence.findByUsername(username);
	}

	@Override
	public List<UserInfo> findByUsernameOrProfileNationalIdNoOrProfileEmail(String username, String nationalIdNo,
			String email) {
		return persistence.findByUsernameOrProfileNationalIdNoOrProfileEmail(username, nationalIdNo, email);
	}

	@Override
	public List<UserInfo> findAll(Specification<UserInfo> specification) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserInfo findByRefreshToken(String refreshToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserInfo> findByUsernameContaining(String username) {
		// TODO Auto-generated method stub
		return null;
	}

}
