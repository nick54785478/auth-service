package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.assembler.GroupAssembler;
import com.example.demo.application.shared.dto.GroupInfoQueried;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.service.GroupService;
import com.example.demo.domain.shared.summary.GroupInfoQueriedSummary;
import com.example.demo.infra.repository.GroupInfoRepository;
import com.example.demo.infra.spec.GetGroupsSummarySpecification;

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
	 * @param service    服務
	 * @param type       群組種類
	 * @param name       群組名稱
	 * @param activeFlag 是否生效
	 * @return List<GroupInfoQueried>
	 */
	@Transactional(readOnly = true)
	public List<GroupInfoQueried> summary(String service, String type, String name, String activeFlag) {
		GetGroupsSummarySpecification specification = new GetGroupsSummarySpecification(service, type, name,
				activeFlag);
		List<GroupInfo> groups = groupInfoRepository.findAll(specification.toSpecification());
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
