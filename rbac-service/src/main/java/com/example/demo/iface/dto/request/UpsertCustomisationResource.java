package com.example.demo.iface.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertCustomisationResource {
	
	private String username;

	private String component;
	
	private String type;
	
	private List<Object> valueList;
}
