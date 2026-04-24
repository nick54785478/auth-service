package com.example.demo.domain.group.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGroupCommand {
	
	private Long id;
	
	private String type;
	
	private String code; // 群組代號
	
	private String name; // 名稱

	private String description; // 敘述
	

}
