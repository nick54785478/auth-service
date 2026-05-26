package com.example.demo.infra.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.demo.domain.setting.aggregate.Setting;
import com.example.demo.domain.setting.repository.SettingRepository;
import com.example.demo.infra.persistence.SettingPersistence;
import com.example.demo.infra.spec.GetSettingsSpecification;
import com.example.demo.shared.enums.YesNo;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
class SettingRepositoryAdapter implements SettingRepository {

	private SettingPersistence persistence;

	@Override
	public Optional<Setting> findById(Long id) {
		return persistence.findById(id);
	}

	@Override
	public Setting save(Setting setting) {
		return persistence.save(setting);
	}

	@Override
	public List<Setting> saveAll(List<Setting> settings) {
		return persistence.saveAll(settings);
	}

	@Override
	public List<Setting> findByScopeServiceAndDataTypeAndActiveFlag(String service, String dataType, YesNo activeFlag) {
		return persistence.findByScopeServiceAndDataTypeAndActiveFlag(service, dataType, activeFlag);
	}

	@Override
	public List<Setting> summary(String service, String dataType, String type, String name, String activeFlag) {
		GetSettingsSpecification specification = new GetSettingsSpecification(service, dataType, type, name,
				activeFlag);
		return persistence.findAll(specification.toSpecification());
	}

}
