package com.example.demo.infra.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.user.aggregate.UserInfo;;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

	List<UserInfo> findByIdIn(List<Long> ids);

	UserInfo findByUsername(String username);

	List<UserInfo> findByUsernameOrProfileNationalIdNoOrProfileEmail(String username, String nationalIdNo,
			String email);

	List<UserInfo> findAll(Specification<UserInfo> specification);

	UserInfo findByRefreshToken(String refreshToken);

	List<UserInfo> findByUsernameContaining(String username);
}
