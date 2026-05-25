package com.example.demo.infra.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.demo.application.shared.query.GetRoleByServiceAndKeywordQuery;
import com.example.demo.application.shared.query.GetRolesSummaryQuery;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.repository.RoleInfoRepository;
import com.example.demo.infra.persistence.RoleInfoPersistence;
import com.example.demo.infra.spec.GetRoleOptionsSpecification;
import com.example.demo.infra.spec.GetRolesSummarySpecification;
import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
class RoleInfoRepositoryAdapter implements RoleInfoRepository {

	private RoleInfoPersistence persistence;

	@Override
	public Optional<RoleInfo> findById(Long id) {
		return persistence.findById(id);
	}

	@Override
	public RoleInfo save(RoleInfo role) {
		return persistence.save(role);
	}

	@Override
	public List<RoleInfo> saveAll(List<RoleInfo> roles) {
		return persistence.saveAll(roles);
	}

	@Override
	public List<RoleInfo> findByIdIn(List<Long> ids) {
		return persistence.findByIdIn(ids);
	}

	@Override
	public List<RoleInfo> findByActiveFlag(YesNo activeFlag) {
		return persistence.findByActiveFlag(activeFlag);
	}

	@Override
	public List<RoleInfo> findByScopeServiceAndScopeCodeInAndActiveFlag(String service, List<String> codes,
			YesNo activeFlag) {
		return persistence.findByScopeServiceAndScopeCodeInAndActiveFlag(service, codes, activeFlag);
	}

	@Override
	public List<RoleInfo> findByScopeServiceAndActiveFlag(String service, YesNo activeFlag) {
		return persistence.findByScopeServiceAndActiveFlag(service, activeFlag);
	}

	@Override
	public List<RoleInfo> findByIdInAndScopeServiceAndActiveFlag(List<Long> ids, String service, YesNo activeFlag) {
		return persistence.findByIdInAndScopeServiceAndActiveFlag(ids, service, activeFlag);
	}

	@Override
	public List<RoleInfo> findByIdInAndActiveFlag(List<Long> ids, YesNo activeFlag) {
		return persistence.findByIdInAndActiveFlag(ids, activeFlag);
	}

	@Override
	public List<RoleInfo> findAll(GetRolesSummaryQuery query) {
		GetRolesSummarySpecification specification = new GetRolesSummarySpecification(query.getService(),
				query.getType(), query.getName(), query.getActiveFlag());
		return persistence.findAll(specification.toSpecification());
	}

	@Override
	public List<RoleInfo> findByServiceAndKeyword(GetRoleByServiceAndKeywordQuery query) {
		GetRoleOptionsSpecification specification = new GetRoleOptionsSpecification(query.getService(),
				query.getService());
		return persistence.findAll(specification.toSpecification());
	}
}
