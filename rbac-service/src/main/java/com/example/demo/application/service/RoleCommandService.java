package com.example.demo.application.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.shared.command.CreateRoleCommand;
import com.example.demo.application.shared.command.UpdateRoleCommand;
import com.example.demo.application.shared.command.UpsertRoleCommand;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.aggregate.vo.RoleProfile;
import com.example.demo.domain.role.aggregate.vo.RoleScope;
import com.example.demo.domain.role.repository.RoleInfoRepository;
import com.example.demo.infra.exception.ValidationException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class RoleCommandService {

	private RoleInfoRepository roleInfoRepository;

	/**
	 * 建立角色資料
	 * 
	 * @param command {@link CreateRoleCommand}
	 * @return RoleInfoCreated
	 */
	public void create(CreateRoleCommand command) {
		RoleScope roleScope = RoleScope.of(command.getService(), command.getCode());
		RoleProfile roleProfile = RoleProfile.of(command.getName(), command.getDescription());
		RoleInfo roleInfo = RoleInfo.create(roleScope, roleProfile, command.getType());
		roleInfoRepository.save(roleInfo);
	}

	/**
	 * 建立或更新多筆角色資料 (適用於前端的 Inline Editable 功能)
	 * 
	 * @param command {@link UpsertRoleCommand} 清單
	 */
	public void upsert(List<UpsertRoleCommand> commands) {

		// 取得 id 清單
		List<Long> ids = commands.stream().filter(command -> command.getId() != null).map(UpsertRoleCommand::getId)
				.collect(Collectors.toList());

		// 取出清單相對應資料
		List<RoleInfo> roles = roleInfoRepository.findByIdIn(ids);

		Map<Long, RoleInfo> map = roles.stream().collect(Collectors.toMap(RoleInfo::getId, Function.identity()));

		List<RoleInfo> roleList = commands.stream().map(command -> {
			// 建立 Role Scope 及 Role Profile
			RoleScope scope = RoleScope.of(command.getService(), command.getCode());
			RoleProfile profile = RoleProfile.of(command.getName(), command.getDescription());

			// 修改
			if (!Objects.isNull(command.getId()) && !Objects.isNull(map.get(command.getId()))) {
				RoleInfo role = map.get(command.getId());
				role.update(scope, profile, command.getType());
				return role;
			} else {
				// 新增
				return RoleInfo.create(scope, profile, command.getType());
			}
		}).collect(Collectors.toList());

		roleInfoRepository.saveAll(roleList);
	}

	/**
	 * 更新角色資料
	 * 
	 * @param command {@link UpdateRoleCommand}
	 */
	public void update(UpdateRoleCommand command) {

		RoleScope roleScope = RoleScope.of(command.getService(), command.getCode());
		RoleProfile roleProfile = RoleProfile.of(command.getName(), command.getDescription());

		Optional<RoleInfo> opt = roleInfoRepository.findById(command.getId());
		if (opt.isPresent()) {
			RoleInfo roleInfo = opt.get();
			roleInfo.update(roleScope, roleProfile, command.getType());
			roleInfoRepository.save(roleInfo);
		} else {
			throw new ValidationException("VALIDATE_FAILED", "查無此角色資料 id，更新失敗");
		}
	}

	/**
	 * 刪除多筆角色資料
	 * 
	 * @param ids 要被刪除的 id 清單
	 */
	public void delete(List<Long> ids) {
		List<RoleInfo> roles = roleInfoRepository.findByIdIn(ids);
		roles.stream().forEach(RoleInfo::delete);
		roleInfoRepository.saveAll(roles);
	}

}
