package com.example.demo.domain.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.aggregate.entity.RoleFunction;
import com.example.demo.domain.role.command.CreateOrUpdateRoleCommand;
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
	 * 建立多筆角色資訊(僅限於前端使用 Inline-Edit)
	 * 
	 * @param command
	 */
	public void createOrUpdate(List<CreateOrUpdateRoleCommand> commands) {

		// 取得 id 清單
		List<Long> ids = commands.stream().filter(command -> command.getId() != null)
				.map(CreateOrUpdateRoleCommand::getId).collect(Collectors.toList());

		// 取出清單相對應資料
		List<RoleInfo> roles = roleInfoRepository.findByIdIn(ids);

		Map<Long, RoleInfo> map = roles.stream().collect(Collectors.toMap(RoleInfo::getId, Function.identity()));

		List<RoleInfo> roleList = commands.stream().map(command -> {

			// 修改
			if (!Objects.isNull(command.getId()) && !Objects.isNull(map.get(command.getId()))) {
				RoleInfo role = map.get(command.getId());
				role.update(command);
				return role;
			} else {
				// 新增
				RoleInfo role = new RoleInfo();
				role.create(command);
				return role;
			}
		}).collect(Collectors.toList());

		roleInfoRepository.saveAll(roleList);
	}

	/**
	 * 查詢符合條件的角色資料
	 * 
	 * @param id      角色ID
	 * @param service 服務
	 * @return RoleInfoQueried
	 */
	public RoleInfoQueriedSummary getRoleInfo(Long id, String service) {
		Optional<RoleInfo> opt = roleInfoRepository.findById(id);
		if (opt.isPresent()) {
			RoleInfo role = opt.get();
			List<Long> funcIds = role.getFunctions().stream().filter(e -> Objects.equals(e.getActiveFlag(), YesNo.Y))
					.map(RoleFunction::getFunctionId).collect(Collectors.toList());
			List<FunctionInfo> functions = functionInfoRepository.findByIdInAndServiceAndActiveFlag(funcIds, service,
					YesNo.Y);
			List<RoleFunctionQueriedDetail> roleFunctionList = functions.stream().map(roleFunction -> {
				return new RoleFunctionQueriedDetail(roleFunction.getId(), roleFunction.getType(),
						roleFunction.getCode(), roleFunction.getName(), roleFunction.getActionType().getLabel(),
						roleFunction.getDescription(), roleFunction.getActiveFlag().name());
			}).collect(Collectors.toList());

			return RoleInfoQueriedSummary.builder().id(id).service(service).code(role.getCode())
					.description(role.getDescription()).name(role.getName()).type(role.getType())
					.functions(roleFunctionList).activeFlag(role.getActiveFlag().getValue()).build();
		} else {
			throw new ValidationException("VALIDATION_FAILED", "該角色 ID 有誤，查詢失敗");
		}
	}

}
