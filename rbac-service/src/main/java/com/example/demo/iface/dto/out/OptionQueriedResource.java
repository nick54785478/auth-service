package com.example.demo.iface.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionQueriedResource {

	  private Long id;

	  private String label;

	  private String value;

	  private String labelTw;

	  private String labelCn;
	 
	  private String labelUs;
}
