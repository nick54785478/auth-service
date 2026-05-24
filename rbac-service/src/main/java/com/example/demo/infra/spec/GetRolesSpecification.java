package com.example.demo.infra.spec;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.example.demo.domain.role.aggregate.RoleInfo;

import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRolesSpecification {

	private String service;

	private String type;

	private String name;

	private String activeFlag;

	/**
	 * 轉換為 JPA Specification
	 */
	public Specification<RoleInfo> toSpecification() {

		return ((root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (StringUtils.isNotBlank(service)) {
				predicates.add(cb.equal(root.get("scope").get("service"), service));
			}

			if (StringUtils.isNotBlank(type)) {
				predicates.add(cb.equal(root.get("type"), type));
			}

			if (StringUtils.isNotBlank(name)) {
				Predicate preName = cb.like(root.get("profile").get("name"), "%" + name + "%");
				Predicate preDesc = cb.like(root.get("profile").get("description"), "%" + name + "%");
				Predicate combinedPredicate = cb.or(preName, preDesc);
				predicates.add(combinedPredicate);
			}

			if (StringUtils.isNotBlank(activeFlag)) {
				predicates.add(cb.equal(root.get("activeFlag"), activeFlag));
			} else {
				predicates.add(cb.equal(root.get("activeFlag"), "Y"));
			}

			Predicate[] predicateArray = new Predicate[predicates.size()];
			query.where(cb.and(predicates.toArray(predicateArray)));
			return query.getRestriction();
		});

	}

}
