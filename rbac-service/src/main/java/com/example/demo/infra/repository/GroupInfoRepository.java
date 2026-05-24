package com.example.demo.infra.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.shared.enums.YesNo;

public interface GroupInfoRepository extends JpaRepository<GroupInfo, Long> {

	List<GroupInfo> findByIdIn(List<Long> ids);

	List<GroupInfo> findByIdInAndActiveFlag(List<Long> ids, YesNo activeFlag);

	// 原本：findByIdInAndServiceAndActiveFlag
	List<GroupInfo> findByIdInAndScopeServiceAndActiveFlag(List<Long> ids, String service, YesNo activeFlag);

	List<GroupInfo> findByActiveFlag(YesNo activeFlag);

	// 原本：findByServiceAndActiveFlag
	// 修改：service 移入 scope
	List<GroupInfo> findByScopeServiceAndActiveFlag(String service, YesNo activeFlag);

	List<GroupInfo> findAll(Specification<GroupInfo> specification);

}
