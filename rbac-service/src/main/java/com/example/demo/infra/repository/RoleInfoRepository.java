package com.example.demo.infra.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.shared.enums.YesNo;

public interface RoleInfoRepository extends JpaRepository<RoleInfo, Long> {

	List<RoleInfo> findByIdIn(List<Long> ids);

	List<RoleInfo> findByActiveFlag(YesNo activeFlag);

	List<RoleInfo> findByScopeServiceAndScopeCodeInAndActiveFlag(String service, List<String> codes, YesNo activeFlag);

	List<RoleInfo> findByScopeServiceAndActiveFlag(String service, YesNo activeFlag);

	List<RoleInfo> findByIdInAndScopeServiceAndActiveFlag(List<Long> ids, String service, YesNo activeFlag);

	List<RoleInfo> findByIdInAndActiveFlag(List<Long> ids, YesNo activeFlag);

	List<RoleInfo> findAll(Specification<RoleInfo> specification);

}
