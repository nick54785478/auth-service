package com.example.demo.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.example.demo.application.shared.command.UpdateRoleFunctionsCommand;
import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.aggregate.entity.RoleFunction;
import com.example.demo.domain.shared.summary.RolesFunctionsQueriedSummary;
import com.example.demo.infra.exception.ValidationException;
import com.example.demo.infra.repository.FunctionInfoRepository;
import com.example.demo.infra.repository.RoleInfoRepository;
import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleFunctionService {

	private RoleInfoRepository roleInfoRepository;
	private FunctionInfoRepository functionInfoRepository;

	/**
	 * 查詢該角色清單所具備的相關功能權限
	 * 
	 * @param service  服務
	 * @param roleList 角色清單
	 * @return RolesFunctionsQueried
	 */
	public RolesFunctionsQueriedSummary getFunctionsByRoleIds(String service, List<String> rolesList) {
		List<RoleInfo> roles = roleInfoRepository.findByScopeServiceAndScopeCodeInAndActiveFlag(service, rolesList,
				YesNo.Y);
		// 取得所有角色清單所帶有的功能 ID
		Set<Long> allFuncIds = roles.stream().flatMap(role -> role.getFunctions().stream())
				.map(RoleFunction::getFunctionId).collect(Collectors.toSet());
		// 取得 Function 清單
		List<FunctionInfo> functions = functionInfoRepository
				.findByIdInAndServiceAndActiveFlag(new ArrayList<>(allFuncIds), service, YesNo.Y);
		return new RolesFunctionsQueriedSummary(service, rolesList, functions);
	}

	/**
	 * 查詢該角色的功能
	 * 
	 * @param id      Role id
	 * @param service Service
	 * @return List<FunctionInfo>
	 */
	public List<FunctionInfo> getRoleFunctions(Long id, String service) {
		Optional<RoleInfo> opt = roleInfoRepository.findById(id);

		if (opt.isEmpty()) {
			return new ArrayList<>();
		} else {
			RoleInfo role = opt.get();
			List<Long> functionIds = role.getFunctions().stream().filter(e -> e.getActiveFlag() == YesNo.Y)
					.map(RoleFunction::getFunctionId).collect(Collectors.toList());
			return functionInfoRepository.findByIdIn(functionIds).stream().filter(e -> service.equals(e.getService()))
					.collect(Collectors.toList());
		}
	}

	/**
	 * 查詢該角色不具備的其他功能
	 * 
	 * @param id      Role id
	 * @param service Service
	 * @return List<FunctionInfo>
	 */
	public List<FunctionInfo> queryOthers(Long id, String service) {
		Optional<RoleInfo> opt = roleInfoRepository.findById(id);
		if (opt.isPresent()) {
			RoleInfo role = opt.get();

			// 篩選出該角色有的 Function ID 清單
			List<Long> existingIds = role.getFunctions().stream().map(RoleFunction::getFunctionId)
					.collect(Collectors.toList());

			// 查出該角色功能資料清單
			List<FunctionInfo> functions = functionInfoRepository.findByServiceAndActiveFlag(service, YesNo.Y);

			// 過濾出該角色所沒有的功能資料
			List<FunctionInfo> filtered = functions.stream().filter(e -> !existingIds.contains(e.getId()))
					.collect(Collectors.toList());

			// 過濾出該角色 ActiveFlag = 'N' 的功能資料
			List<Long> inactiveRelatedIds = role.getFunctions().stream()
					.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.N.getValue()))
					.map(RoleFunction::getFunctionId).collect(Collectors.toList());

			// 過濾出該角色資料但失效的功能資料 activeFlag = 'N'
			List<FunctionInfo> inactiveRelated = functions.stream().filter(e -> inactiveRelatedIds.contains(e.getId()))
					.collect(Collectors.toList());

			// 合併兩者
			filtered.addAll(inactiveRelated);
			return filtered;

		} else {
			throw new ValidationException("VALIDATE_FAILED", "該角色 ID 有誤，查詢失敗");
		}

	}

	/**
	 * 賦予角色相關功能權限
	 * 
	 * @param command {@link UpdateRoleFunctionsCommand}
	 */
	public void update(UpdateRoleFunctionsCommand command) {
		// 透過功能 ID 清單取得功能
		List<FunctionInfo> functions = functionInfoRepository.findByIdInAndActiveFlag(command.getFunctions(), YesNo.Y);

		// 建立 Role Function 資料清單
		List<RoleFunction> roleFunctions = functions.stream().map(function -> {
			RoleFunction roleFunction = new RoleFunction();
			roleFunction.create(command.getRoleId(), function.getId());
			return roleFunction;
		}).collect(Collectors.toList());

		// 透過 Role Id 取得 角色資料
		roleInfoRepository.findById(command.getRoleId()).ifPresent(role -> {
			role.updateFunctions(roleFunctions);
			roleInfoRepository.save(role);
		});
	}

}
