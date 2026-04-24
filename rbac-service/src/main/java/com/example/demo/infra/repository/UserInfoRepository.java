package com.example.demo.infra.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.user.aggregate.UserInfo;

import jakarta.persistence.criteria.Predicate;;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

	List<UserInfo> findByIdIn(List<Long> ids);
	
	UserInfo findByUsername(String username);
	
	List<UserInfo> findByUsernameOrNationalIdNoOrEmail(String username, String nationalIdNo, String email);
	
	List<UserInfo> findAll(Specification<UserInfo> specification);
	
	UserInfo findByRefreshToken(String refreshToken);

	default List<UserInfo> findAllWithSpecification(String str) {
		Specification<UserInfo> specification = ((root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.isNotBlank(str)) {
				predicates.add(cb.like(root.get("username"), "%" + str + "%"));
			}
			if (StringUtils.isNotBlank(str)) {
				predicates.add(cb.like(root.get("name"),  "%" + str + "%"));
			}
			Predicate[] predicateArray = new Predicate[predicates.size()];
			query.where(cb.or(predicates.toArray(predicateArray)));
			return query.getRestriction();
		});
		return findAll(specification);
	}
}
