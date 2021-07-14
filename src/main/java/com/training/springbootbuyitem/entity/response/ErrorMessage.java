package com.training.springbootbuyitem.entity.response;

import lombok.Data;

@Data
public class ErrorMessage {

	public ErrorMessage(){}

	private String traceId;
	private String operation;
	private int code;
	private String message;

}
