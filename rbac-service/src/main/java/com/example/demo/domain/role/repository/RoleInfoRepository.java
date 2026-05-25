package com.example.demo.domain.role.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.infra.spec.GetRolesSummarySpecification;
import com.example.demo.shared.enums.YesNo;

public interface RoleInfoRepository {

	Optional<RoleInfo> findById(Long id);

	RoleInfo save(RoleInfo role);

	List<RoleInfo> saveAll(List<RoleInfo> roles);

	List<RoleInfo> findByIdIn(List<Long> ids);

	List<RoleInfo> findByActiveFlag(YesNo activeFlag);

	List<RoleInfo> findByScopeServiceAndScopeCodeInAndActiveFlag(String service, List<String> codes, YesNo activeFlag);

	List<RoleInfo> findByScopeServiceAndActiveFlag(String service, YesNo activeFlag);

	List<RoleInfo> findByIdInAndScopeServiceAndActiveFlag(List<Long> ids, String service, YesNo activeFlag);

	List<RoleInfo> findByIdInAndActiveFlag(List<Long> ids, YesNo activeFlag);

	List<RoleInfo> findAll(GetRolesSummarySpecification specification);
}
