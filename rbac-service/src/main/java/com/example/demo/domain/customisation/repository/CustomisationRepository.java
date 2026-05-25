package com.example.demo.domain.customisation.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.domain.customisation.aggregate.Customisation;

public interface CustomisationRepository {

	Optional<Customisation> findById(Long id);

	Customisation save(Customisation customisation);

	List<Customisation> saveAll(List<Customisation> customisations);

	Optional<Customisation> findByScopeUsernameAndScopeComponentAndScopeType(String username, String component,
			String type);

}
