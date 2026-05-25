package com.example.demo.infra.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.function.aggregate.vo.FunctionScope;
import com.example.demo.domain.function.repository.FunctionInfoRepository;
import com.example.demo.infra.persistence.FunctionInfoPersistence;
import com.example.demo.infra.spec.GetFunctionsSummarySpecification;
import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
class FunctionInfoRepositoryAdapter implements FunctionInfoRepository {

	private FunctionInfoPersistence persistence;

	@Override
	public Optional<FunctionInfo> findById(Long id) {
		return persistence.findById(id);
	}

	@Override
	public FunctionInfo save(FunctionInfo function) {
		return persistence.save(function);
	}

	@Override
	public List<FunctionInfo> saveAll(List<FunctionInfo> functions) {
		return persistence.saveAll(functions);
	}

	@Override
	public List<FunctionInfo> findByIdIn(List<Long> ids) {
		return persistence.findByIdIn(ids);
	}

	@Override
	public List<FunctionInfo> findByActiveFlag(YesNo activeFlag) {
		return persistence.findByActiveFlag(activeFlag);
	}

	@Override
	public List<FunctionInfo> findByScopeServiceAndActiveFlag(String service, YesNo activeFlag) {
		return persistence.findByScopeServiceAndActiveFlag(service, activeFlag);
	}

	@Override
	public List<FunctionInfo> findByTypeAndActiveFlag(String type, YesNo activeFlag) {
		return persistence.findByTypeAndActiveFlag(type, activeFlag);
	}

	@Override
	public List<FunctionInfo> findByIdInAndActiveFlag(List<Long> ids, YesNo activeFlag) {
		return persistence.findByIdInAndActiveFlag(ids, activeFlag);
	}

	@Override
	public List<FunctionInfo> findByIdInAndScopeServiceAndActiveFlag(List<Long> ids, String service, YesNo activeFlag) {
		return persistence.findByIdInAndScopeServiceAndActiveFlag(ids, service, activeFlag);
	}

	@Override
	public Optional<FunctionInfo> findByScopeServiceAndScopeCode(String service, String code) {
		return persistence.findByScopeServiceAndScopeCode(service, code);
	}

	@Override
	public boolean existsByScope(FunctionScope scope) {
		return persistence.existsByScope(scope);
	}

	@Override
	public List<FunctionInfo> findByIdInAndTypeAndActiveFlag(List<Long> ids, String type, YesNo activeFlag) {
		return persistence.findByIdInAndTypeAndActiveFlag(ids, type, activeFlag);
	}

	@Override
	public List<FunctionInfo> findAll(GetFunctionsSummarySpecification specification) {
		return persistence.findAll(specification.toSpecification());
	}

}
