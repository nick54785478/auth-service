package com.example.demo.infra.persistence;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.setting.aggregate.Setting;
import com.example.demo.shared.enums.YesNo;

public interface SettingPersistence extends JpaRepository<Setting, Long> {

	List<Setting> findByScopeServiceAndDataTypeAndActiveFlag(String service, String dataType, YesNo activeFlag);

	List<Setting> findAll(Specification<Setting> specification);

}
