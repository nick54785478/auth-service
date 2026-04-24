package com.example.demo.domain.user.command;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserCommand {
	
	private Long id;

	private String name;

	private String email;

	private Date birthday;
	
	private String nationalId;

	private String address;
	
}
