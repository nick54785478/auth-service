package com.example.demo.domain.customisation.command;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomisationCommand {
	
	private String username;

	private String component;
	
	private String type;
	
	private List<Object> valueList = new ArrayList<>();
	
}
