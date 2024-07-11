package com.jads.exception;

import java.util.Date;

public class ExceptionResponse {
	
	private Date timestamp;
	private String message;
	private String debug;
	
	public ExceptionResponse(Date timestamp, String message, String debug) {
		this.timestamp = timestamp;
		this.message = message;
		this.debug = debug;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDebug() {
		return debug;
	}

}
