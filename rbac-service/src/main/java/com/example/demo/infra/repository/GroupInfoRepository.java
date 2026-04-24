package com.example.demo.infra.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.shared.enums.YesNo;

import jakarta.persistence.criteria.Predicate;

@Repository
public interface GroupInfoRepository extends JpaRepository<GroupInfo, Long> {

	List<GroupInfo> findByIdIn(List<Long> ids);

	List<GroupInfo> findByIdInAndActiveFlag(List<Long> ids, YesNo activeFlag);

	List<GroupInfo> findByIdInAndServiceAndActiveFlag(List<Long> ids, String service, YesNo activeFlag);

	List<GroupInfo> findByActiveFlag(YesNo activeFlag);
	
	List<GroupInfo> findByServiceAndActiveFlag(String service, YesNo activeFlag);

	List<GroupInfo> findAll(Specification<GroupInfo> specification);

	default List<GroupInfo> findAllWithSpecification(String service, String type, String name, String activeFlag) {
		Specification<GroupInfo> specification = ((root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (StringUtils.isNotBlank(service)) {
				predicates.add(cb.equal(root.get("service"), service));
			}

			if (StringUtils.isNotBlank(type)) {
				predicates.add(cb.equal(root.get("type"), type));
			}

			if (StringUtils.isNotBlank(name)) {
				Predicate preName = cb.like(root.get("name"), "%" + name + "%");
				Predicate preDesc = cb.like(root.get("description"), "%" + name + "%");
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
		return findAll(specification);
	}

	default List<GroupInfo> findAllWithSpecification(String service, String str) {
		Specification<GroupInfo> specification = ((root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.isNotBlank(service)) {
				predicates.add(cb.equal(root.get("service"), service));
			}
			if (StringUtils.isNotBlank(str)) {
				Predicate predName = cb.like(root.get("name"), "%" + str + "%");
				Predicate predCode = cb.like(root.get("code"), "%" + str + "%");
				Predicate combinedPredicate = cb.or(predName, predCode);
				predicates.add(combinedPredicate);
			}

			predicates.add(cb.equal(root.get("activeFlag"), "Y"));

			Predicate[] predicateArray = new Predicate[predicates.size()];
			query.where(cb.and(predicates.toArray(predicateArray)));
			return query.getRestriction();
		});
		return findAll(specification);
	}
}
