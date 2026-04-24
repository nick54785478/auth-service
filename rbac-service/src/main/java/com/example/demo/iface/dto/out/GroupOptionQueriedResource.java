package com.example.demo.iface.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupOptionQueriedResource {

	private Long id;

	private String code; // 群組 Code

	private String name;

}
