package com.example.demo.domain.group.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.application.shared.query.GetGroupByServiceAndKeywordQuery;
import com.example.demo.application.shared.query.GetGroupsSummaryQuery;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.shared.enums.YesNo;

public interface GroupInfoRepository {

	Optional<GroupInfo> findById(Long id);

	GroupInfo save(GroupInfo group);

	List<GroupInfo> saveAll(List<GroupInfo> groups);

	List<GroupInfo> findByIdIn(List<Long> ids);

	List<GroupInfo> findByIdInAndActiveFlag(List<Long> ids, YesNo activeFlag);

	List<GroupInfo> findByIdInAndScopeServiceAndActiveFlag(List<Long> ids, String service, YesNo activeFlag);

	List<GroupInfo> findByActiveFlag(YesNo activeFlag);

	List<GroupInfo> findByScopeServiceAndActiveFlag(String service, YesNo activeFlag);

	List<GroupInfo> findAll(GetGroupsSummaryQuery query);

	List<GroupInfo> findByServiceAndKeyword(GetGroupByServiceAndKeywordQuery query);
}
