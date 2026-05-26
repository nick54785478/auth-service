package com.example.demo.domain.setting.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.domain.setting.aggregate.Setting;
import com.example.demo.shared.enums.YesNo;

public interface SettingRepository {

	Optional<Setting> findById(Long id);

	Setting save(Setting setting);

	List<Setting> saveAll(List<Setting> settings);

	List<Setting> findByScopeServiceAndDataTypeAndActiveFlag(String service, String dataType, YesNo activeFlag);

	List<Setting> summary(String service, String dataType, String type, String name, String activeFlag);
}
