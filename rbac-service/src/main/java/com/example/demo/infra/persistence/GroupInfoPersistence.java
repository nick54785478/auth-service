package com.example.demo.infra.persistence;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.shared.enums.YesNo;

public interface GroupInfoPersistence extends JpaRepository<GroupInfo, Long> {

	List<GroupInfo> findByIdIn(List<Long> ids);

	List<GroupInfo> findByIdInAndActiveFlag(List<Long> ids, YesNo activeFlag);

	List<GroupInfo> findByIdInAndScopeServiceAndActiveFlag(List<Long> ids, String service, YesNo activeFlag);

	List<GroupInfo> findByActiveFlag(YesNo activeFlag);

	List<GroupInfo> findByScopeServiceAndActiveFlag(String service, YesNo activeFlag);

	List<GroupInfo> findAll(Specification<GroupInfo> specification);

}
