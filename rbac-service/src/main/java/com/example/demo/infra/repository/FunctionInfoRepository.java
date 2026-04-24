package com.example.demo.infra.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.shared.enums.YesNo;

import jakarta.persistence.criteria.Predicate;

@Repository
public interface FunctionInfoRepository extends JpaRepository<FunctionInfo, Long> {

	List<FunctionInfo> findByIdIn(List<Long> ids);

	List<FunctionInfo> findByActiveFlag(YesNo activeFlag);

	List<FunctionInfo> findByServiceAndActiveFlag(String service, YesNo activeFlag);

	List<FunctionInfo> findByTypeAndActiveFlag(String type, YesNo activeFlag);
	
	List<FunctionInfo> findByIdInAndActiveFlag(List<Long> ids, YesNo activeFlag);

	List<FunctionInfo> findByIdInAndServiceAndActiveFlag(List<Long> ids, String service, YesNo activeFlag);

	List<FunctionInfo> findByIdInAndTypeAndActiveFlag(List<Long> ids, String type, YesNo activeFlag);

	List<FunctionInfo> findAll(Specification<FunctionInfo> specification);

	default List<FunctionInfo> findAllWithSpecification(String service, String actionType, String type, String name,
			String activeFlag) {
		Specification<FunctionInfo> specification = ((root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.isNotBlank(service)) {
				predicates.add(cb.equal(root.get("service"), service));
			}
			if (StringUtils.isNotBlank(type)) {
				predicates.add(cb.equal(root.get("type"), type));
			}
			if (StringUtils.isNotBlank(actionType)) {
				predicates.add(cb.equal(root.get("actionType"), actionType));
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

	default List<FunctionInfo> findAllWithSpecification(String queryStr) {
		Specification<FunctionInfo> specification = ((root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (StringUtils.isNotBlank(queryStr)) {
				Predicate preType = cb.like(root.get("type"), "%" + queryStr + "%");
				Predicate preName = cb.like(root.get("name"), "%" + queryStr + "%");
				Predicate preDesc = cb.like(root.get("description"), "%" + queryStr + "%");
				Predicate combinedPredicate = cb.or(preName, preDesc, preType);
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
