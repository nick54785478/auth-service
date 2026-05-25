package com.example.demo.domain.function.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.function.aggregate.vo.FunctionScope;
import com.example.demo.infra.spec.GetFunctionsSummarySpecification;
import com.example.demo.shared.enums.YesNo;

public interface FunctionInfoRepository {

	Optional<FunctionInfo> findById(Long id);

	FunctionInfo save(FunctionInfo function);

	List<FunctionInfo> saveAll(List<FunctionInfo> functions);

	List<FunctionInfo> findByIdIn(List<Long> ids);

	List<FunctionInfo> findByActiveFlag(YesNo activeFlag);

	List<FunctionInfo> findByScopeServiceAndActiveFlag(String service, YesNo activeFlag);

	List<FunctionInfo> findByTypeAndActiveFlag(String type, YesNo activeFlag);

	List<FunctionInfo> findByIdInAndActiveFlag(List<Long> ids, YesNo activeFlag);

	List<FunctionInfo> findByIdInAndScopeServiceAndActiveFlag(List<Long> ids, String service, YesNo activeFlag);

	Optional<FunctionInfo> findByScopeServiceAndScopeCode(String service, String code);

	boolean existsByScope(FunctionScope scope);

	List<FunctionInfo> findByIdInAndTypeAndActiveFlag(List<Long> ids, String type, YesNo activeFlag);

	List<FunctionInfo> findAll(GetFunctionsSummarySpecification specification);
}
