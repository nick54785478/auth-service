package com.example.demo.infra.spec;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.example.demo.domain.function.aggregate.FunctionInfo;

import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetFunctionsOptionsSpecification {

	private String service;

	private String keyword;

	/**
	 * 轉換為 JPA Specification
	 */
	public Specification<FunctionInfo> toSpecification() {

		return ((root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.isNotBlank(service)) {
				predicates.add(cb.equal(root.get("scope").get("service"), service));
			}

			if (StringUtils.isNotBlank(keyword)) {
				Predicate preType = cb.like(root.get("type"), "%" + keyword + "%");
				Predicate preName = cb.like(root.get("profile").get("name"), "%" + keyword + "%");
				Predicate preDesc = cb.like(root.get("profile").get("description"), "%" + keyword + "%");
				Predicate combinedPredicate = cb.or(preName, preDesc, preType);
				predicates.add(combinedPredicate);
			}

			predicates.add(cb.equal(root.get("activeFlag"), "Y"));

			Predicate[] predicateArray = new Predicate[predicates.size()];
			query.where(cb.and(predicates.toArray(predicateArray)));
			return query.getRestriction();
		});

	}
}
