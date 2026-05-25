package com.example.demo.infra.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.demo.domain.customisation.aggregate.Customisation;
import com.example.demo.domain.customisation.repository.CustomisationRepository;
import com.example.demo.infra.persistence.CustomisationPersistence;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
class CustomisationRepositoryAdapter implements CustomisationRepository {

	private CustomisationPersistence persistence;

	@Override
	public Optional<Customisation> findById(Long id) {
		return persistence.findById(id);
	}

	@Override
	public Customisation save(Customisation customisation) {
		return persistence.save(customisation);
	}

	@Override
	public List<Customisation> saveAll(List<Customisation> customisations) {
		return persistence.saveAll(customisations);
	}

	@Override
	public Optional<Customisation> findByScopeUsernameAndScopeComponentAndScopeType(String username, String component,
			String type) {
		return persistence.findByScopeUsernameAndScopeComponentAndScopeType(username, component, type);
	}

}
