package com.example.demo.infra.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.customisation.aggregate.Customisation;

@Repository
public interface CustomisationPersistence extends JpaRepository<Customisation, Long> {

	Optional<Customisation> findByScopeUsernameAndScopeComponentAndScopeType(String username, String component, String type);
}
