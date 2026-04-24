package com.example.demo.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.customisation.aggregate.Customisation;
import com.example.demo.domain.customisation.command.UpdateCustomisationCommand;
import com.example.demo.infra.repository.CustomisationRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class CustomisationCommandService {

	private CustomisationRepository customisationRepository;

	/**
	 * 更新個人客製化設置
	 * 
	 * @param command
	 */
	public void updateCustomisation(UpdateCustomisationCommand command) {
		Customisation customisation = customisationRepository
				.findByUsernameAndComponentAndType(command.getUsername(), command.getComponent(), command.getType())
				.orElseGet(Customisation::new);
		customisation.update(command);
		customisationRepository.save(customisation);
	}

}
