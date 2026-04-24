package com.example.demo.domain.shared.summary;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.domain.shared.detail.GroupRoleQueriedDetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupRolesQueriedSummary {

	private Long groupId;
	
	private List<GroupRoleQueriedDetail> roleList = new ArrayList<>();
}
