package com.example.demo.application.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionOptionQueried {

	private Long id;

	private String code; // 功能 Code

	private String name;

}
