package com.example.demo.infra.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.group.repository.GroupInfoRepository;
import com.example.demo.infra.persistence.GroupInfoPersistence;
import com.example.demo.infra.spec.GetGroupsSummarySpecification;
import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
class GroupInfoRepositoryAdapter implements GroupInfoRepository {

	private GroupInfoPersistence persistence;

	@Override
	public Optional<GroupInfo> findById(Long id) {
		return persistence.findById(id);
	}

	@Override
	public GroupInfo save(GroupInfo group) {
		return persistence.save(group);
	}

	@Override
	public List<GroupInfo> saveAll(List<GroupInfo> groups) {
		return persistence.saveAll(groups);
	}

	@Override
	public List<GroupInfo> findByIdIn(List<Long> ids) {
		return persistence.findByIdIn(ids);
	}

	@Override
	public List<GroupInfo> findByIdInAndActiveFlag(List<Long> ids, YesNo activeFlag) {
		return persistence.findByIdInAndActiveFlag(ids, activeFlag);
	}

	@Override
	public List<GroupInfo> findByIdInAndScopeServiceAndActiveFlag(List<Long> ids, String service, YesNo activeFlag) {
		return persistence.findByIdInAndScopeServiceAndActiveFlag(ids, service, activeFlag);
	}

	@Override
	public List<GroupInfo> findByActiveFlag(YesNo activeFlag) {
		return persistence.findByActiveFlag(activeFlag);
	}

	@Override
	public List<GroupInfo> findByScopeServiceAndActiveFlag(String service, YesNo activeFlag) {
		return persistence.findByScopeServiceAndActiveFlag(service, activeFlag);
	}

	@Override
	public List<GroupInfo> findAll(GetGroupsSummarySpecification specification) {
		return persistence.findAll(specification.toSpecification());
	}

}
