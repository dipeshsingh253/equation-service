package com.frieghtfox.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class GenericErrorResponse<T> {
	
	private T message;
	private LocalDateTime timestamp;

}
