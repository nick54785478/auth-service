package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.assembler.GroupAssembler;
import com.example.demo.application.shared.dto.GroupInfoQueried;
import com.example.demo.application.shared.query.GetGroupsSummaryQuery;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.group.repository.GroupInfoRepository;
import com.example.demo.domain.service.GroupService;
import com.example.demo.domain.shared.summary.GroupInfoQueriedSummary;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class GroupQueryService {

	private GroupAssembler assembler;
	private GroupService groupService;
	private GroupInfoRepository groupInfoRepository;

	/**
	 * 查詢符合條件的群組資料
	 * 
	 * @param query {@link GetGroupsSummaryQuery}
	 * @return List<GroupInfoQueried>
	 */
	@Transactional(readOnly = true)
	public List<GroupInfoQueried> summary(GetGroupsSummaryQuery query) {
		List<GroupInfo> groups = groupInfoRepository.summary(query.getService(), query.getType(), query.getName(),
				query.getActiveFlag());
		log.info("groups: {}", groups);
		return assembler.transformGroups(groups);
	}

	/**
	 * 透過 ID 與服務查詢群組相關資料
	 * 
	 * @param id
	 * @param service 服務
	 * @return List<GroupInfoQueried>
	 */
	@Transactional
	public GroupInfoQueriedSummary getGroupInfo(Long id, String service) {
		GroupInfoQueriedSummary groups = groupService.getGroupInfo(id, service);
		log.info("groups: {}", groups);
		return groups;
	}
}
