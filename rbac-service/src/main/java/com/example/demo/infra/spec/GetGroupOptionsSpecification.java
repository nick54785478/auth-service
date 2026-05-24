package com.example.demo.infra.spec;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.example.demo.domain.group.aggregate.GroupInfo;

import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetGroupOptionsSpecification {

	private String service;
	private String keyword;

	/**
	 * 轉換為 JPA Specification
	 */
	public Specification<GroupInfo> toSpecification() {

		return ((root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.isNotBlank(service)) {
				predicates.add(cb.equal(root.get("scope").get("service"), service));
			}
			if (StringUtils.isNotBlank(keyword)) {
				Predicate predName = cb.like(root.get("profile").get("name"), "%" + keyword + "%");
				Predicate predCode = cb.like(root.get("scope").get("code"), "%" + keyword + "%");
				Predicate combinedPredicate = cb.or(predName, predCode);
				predicates.add(combinedPredicate);
			}

			predicates.add(cb.equal(root.get("activeFlag"), "Y"));

			Predicate[] predicateArray = new Predicate[predicates.size()];
			query.where(cb.and(predicates.toArray(predicateArray)));
			return query.getRestriction();
		});
	}
}
