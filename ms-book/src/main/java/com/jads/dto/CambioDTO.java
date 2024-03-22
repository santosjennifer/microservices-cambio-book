package com.jads.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambioDTO {
	
	private Long id;
	private String from;
	private String to;
	private Double conversionFactor;
	private Double convertedValue;
	private String environment;
	
}
