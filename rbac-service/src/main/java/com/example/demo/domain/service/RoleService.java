package com.example.demo.domain.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.aggregate.entity.RoleFunction;
import com.example.demo.domain.shared.detail.RoleFunctionQueriedDetail;
import com.example.demo.domain.shared.summary.RoleInfoQueriedSummary;
import com.example.demo.infra.exception.ValidationException;
import com.example.demo.infra.repository.FunctionInfoRepository;
import com.example.demo.infra.repository.RoleInfoRepository;
import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleService {

	private FunctionInfoRepository functionInfoRepository;
	private RoleInfoRepository roleInfoRepository;

	/**
	 * 查詢符合條件的角色資料
	 * 
	 * @param id      Role id
	 * @param service Service
	 * @return {@link RoleInfoQueriedSummary}
	 */
	public RoleInfoQueriedSummary getRoleInfo(Long id, String service) {
		Optional<RoleInfo> opt = roleInfoRepository.findById(id);
		if (opt.isPresent()) {
			RoleInfo role = opt.get();
			List<Long> funcIds = role.getFunctions().stream().filter(e -> Objects.equals(e.getActiveFlag(), YesNo.Y))
					.map(RoleFunction::getFunctionId).collect(Collectors.toList());
			List<FunctionInfo> functions = functionInfoRepository.findByIdInAndScopeServiceAndActiveFlag(funcIds,
					service, YesNo.Y);
			List<RoleFunctionQueriedDetail> roleFunctionList = functions.stream().map(roleFunction -> {
				return new RoleFunctionQueriedDetail(roleFunction.getId(), roleFunction.getType(),
						roleFunction.getScope().getCode(), roleFunction.getProfile().getName(),
						roleFunction.getActionType().getLabel(), roleFunction.getProfile().getDescription(),
						roleFunction.getActiveFlag().name());
			}).collect(Collectors.toList());

			return RoleInfoQueriedSummary.builder().id(id).service(service).code(role.getScope().getCode())
					.description(role.getProfile().getDescription()).name(role.getProfile().getName())
					.type(role.getType()).functions(roleFunctionList).activeFlag(role.getActiveFlag().getValue())
					.build();
		} else {
			throw new ValidationException("VALIDATE_FAILED", "該角色 ID 有誤，查詢失敗");
		}
	}

}
