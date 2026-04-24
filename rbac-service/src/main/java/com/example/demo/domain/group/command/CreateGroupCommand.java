package com.example.demo.domain.group.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupCommand {
	
	private String service;
	
	private String type; // 群組種類
 	
	private String code; // 群組代號
	
	private String name; // 名稱

	private String description; // 敘述
	

}
