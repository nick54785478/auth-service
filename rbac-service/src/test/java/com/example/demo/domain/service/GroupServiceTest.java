package com.example.demo.domain.service;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GroupServiceTest {

	@Autowired
	private GroupService groupService;

	/**
	 * 查詢群組角色
	 */
	@Test
	void testQueryRoles() {
		Long groupId = 2L;
		var queryRoles = groupService.queryRoles(groupId);
		System.out.println(queryRoles);
		assertFalse(queryRoles.isEmpty());
	}

}
