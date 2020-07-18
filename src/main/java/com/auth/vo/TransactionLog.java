package com.auth.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionLog {
	private char status;
	private String httpMethod;
	private String uri;
	private String remoteAddr;
	private String classPath;
	private String method;
}
