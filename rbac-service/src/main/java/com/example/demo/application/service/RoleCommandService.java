package com.example.demo.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.command.CreateOrUpdateRoleCommand;
import com.example.demo.domain.role.command.CreateRoleCommand;
import com.example.demo.domain.role.command.UpdateRoleCommand;
import com.example.demo.domain.service.RoleService;
import com.example.demo.infra.exception.ValidationException;
import com.example.demo.infra.repository.RoleInfoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class RoleCommandService {

	private RoleInfoRepository roleInfoRepository;
	private RoleService roleService;

	/**
	 * 建立角色資料
	 * 
	 * @param command
	 * @return RoleInfoCreated
	 */
	public void create(CreateRoleCommand command) {
		RoleInfo roleInfo = new RoleInfo();
		roleInfo.create(command);
		roleInfoRepository.save(roleInfo);
	}

	/**
	 * 建立多筆角色資料
	 * 
	 * @param command
	 * @return RoleInfoCreated
	 */
	public void createOrUpdate(List<CreateOrUpdateRoleCommand> commands) {
		roleService.createOrUpdate(commands);
	}

	/**
	 * 更新角色資料
	 * 
	 * @param command
	 * @return RoleInfoCreated
	 */
	public void update(UpdateRoleCommand command) {

		Optional<RoleInfo> opt = roleInfoRepository.findById(command.getId());
		if (opt.isPresent()) {
			RoleInfo roleInfo = opt.get();
			roleInfo.update(command);
			roleInfoRepository.save(roleInfo);
		} else {
			throw new ValidationException("VALIDATION_FAILED", "查無此角色資料 id，更新失敗");
		}
	}

	/**
	 * 刪除多筆角色資料
	 * 
	 * @param ids 要被刪除的 id 清單
	 */
	public void delete(List<Long> ids) {
		List<RoleInfo> roles = roleInfoRepository.findByIdIn(ids);
		roles.stream().forEach(role -> {
			role.delete();
		});
		roleInfoRepository.saveAll(roles);
	}

}
