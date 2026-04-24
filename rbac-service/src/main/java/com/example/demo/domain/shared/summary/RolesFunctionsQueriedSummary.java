package com.example.demo.domain.shared.summary;

import java.util.List;

import com.example.demo.domain.function.aggregate.FunctionInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolesFunctionsQueriedSummary {

	private String service; // 服務

	private List<String> roleList; // 角色清單(個人及群組)

	private List<FunctionInfo> funcList; // 功能清單
}
