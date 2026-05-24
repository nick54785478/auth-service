package com.example.demo.infra.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.shared.enums.YesNo;

import jakarta.persistence.criteria.Predicate;

@Repository
public interface RoleInfoRepository extends JpaRepository<RoleInfo, Long> {

	List<RoleInfo> findByIdIn(List<Long> ids);

	List<RoleInfo> findByActiveFlag(YesNo activeFlag);

	// 原本：findByServiceAndCodeInAndActiveFlag
	// 修改：service -> scope.service, code -> scope.code
	List<RoleInfo> findByScopeServiceAndScopeCodeInAndActiveFlag(String service, List<String> codes, YesNo activeFlag);

	// 原本：findByServiceAndActiveFlag
	List<RoleInfo> findByScopeServiceAndActiveFlag(String service, YesNo activeFlag);

	// 原本：findByIdInAndServiceAndActiveFlag
	List<RoleInfo> findByIdInAndScopeServiceAndActiveFlag(List<Long> ids, String service, YesNo activeFlag);

//	// 原本：findByIdInAndServiceNot
//	List<RoleInfo> findByIdInAndScopeServiceNot(List<Long> ids, String service);

	List<RoleInfo> findByIdInAndActiveFlag(List<Long> ids, YesNo activeFlag);

//	// 原本：findByServiceNotAndActiveFlag
//	List<RoleInfo> findByScopeServiceNotAndActiveFlag(String service, YesNo activeFlag);

	List<RoleInfo> findAll(Specification<RoleInfo> specification);

	default List<RoleInfo> findAllWithSpecification(String service, String type, String name, String activeFlag) {
		Specification<RoleInfo> specification = ((root, query, cb) -> {
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
		return findAll(specification);
	}

	default List<RoleInfo> findAllWithSpecification(String service, String str) {
		Specification<RoleInfo> specification = ((root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.isNotBlank(service)) {
				predicates.add(cb.equal(root.get("profile").get("service"), service));
			}
			if (StringUtils.isNotBlank(str)) {
				Predicate predName = cb.like(root.get("profile").get("name"), "%" + str + "%");
				Predicate predCode = cb.like(root.get("scope").get("code"), "%" + str + "%");
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
