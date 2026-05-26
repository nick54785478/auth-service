package com.example.demo.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.function.repository.FunctionInfoRepository;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.aggregate.entity.RoleFunction;
import com.example.demo.domain.role.repository.RoleInfoRepository;
import com.example.demo.domain.shared.summary.RolesFunctionsQueriedSummary;
import com.example.demo.infra.exception.ValidationException;
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
				.findByIdInAndScopeServiceAndActiveFlag(new ArrayList<>(allFuncIds), service, YesNo.Y);
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
		return roleInfoRepository.findById(id).map(role -> {
			// 1. 從 Aggregate 中篩選出「啟用中 (YesNo.Y)」的 RoleFunction 關聯，並提取 Function ID
			List<Long> activeFunctionIds = role.getFunctions().stream().filter(e -> e.getActiveFlag() == YesNo.Y)
					.map(RoleFunction::getFunctionId).collect(Collectors.toList());

			if (activeFunctionIds.isEmpty()) {
				return new ArrayList<FunctionInfo>();
			}

			// 2. 直接利用我們剛在 FunctionInfoRepository 改好的方法，
			// 把 in (IDs) + scopeService + activeFlag(Y) 統包給資料庫查詢，減少記憶體內的 filter
			return functionInfoRepository.findByIdInAndScopeServiceAndActiveFlag(activeFunctionIds, service, YesNo.Y);
		}).orElse(new ArrayList<>()); // 找不到 Role 時回傳空陣列
	}

	/**
	 * 查詢該角色不具備的其他功能
	 * 
	 * @param id      Role id
	 * @param service Service
	 * @return List<FunctionInfo>
	 */
	public List<FunctionInfo> queryOthers(Long id, String service) {
		// 1. 查出 Aggregate Root，若無則拋出例外
		RoleInfo role = roleInfoRepository.findById(id)
				.orElseThrow(() -> new ValidationException("VALIDATE_FAILED", "該角色 ID 有誤，查詢失敗"));

		// 2. 篩選出該角色「目前已擁有，且關聯有效 (Y)」的 Function ID 清單
		List<Long> activeAssignedFunctionIds = role.getFunctions().stream().filter(e -> e.getActiveFlag() == YesNo.Y)
				.map(RoleFunction::getFunctionId).collect(Collectors.toList());

		// 3. 查出該 Service 下系統「所有啟用中」的 Function 資料
		List<FunctionInfo> allActiveFunctionsInService = functionInfoRepository.findByScopeServiceAndActiveFlag(service,
				YesNo.Y);

		// 4. 邏輯簡化：全部有效功能 - 角色已擁有的有效功能 = 角色不具備的功能
		return allActiveFunctionsInService.stream().filter(f -> !activeAssignedFunctionIds.contains(f.getId()))
				.collect(Collectors.toList());
	}

	/**
	 * 賦予角色相關功能權限
	 * 
	 * @param roleId  角色 ID
	 * @param funcIds 角色 ID 清單
	 */
	public void update(Long roleId, List<Long> funcIds) {
		// 透過功能 ID 清單取得功能
		List<FunctionInfo> functions = functionInfoRepository.findByIdInAndActiveFlag(funcIds, YesNo.Y);

		// 建立 Role Function 資料清單
		List<RoleFunction> roleFunctions = functions.stream().map(function -> {
			RoleFunction roleFunction = new RoleFunction();
			roleFunction.create(roleId, function.getId());
			return roleFunction;
		}).collect(Collectors.toList());

		// 透過 Role Id 取得 角色資料
		roleInfoRepository.findById(roleId).ifPresent(role -> {
			role.updateFunctions(roleFunctions);
			roleInfoRepository.save(role);
		});
	}

}
