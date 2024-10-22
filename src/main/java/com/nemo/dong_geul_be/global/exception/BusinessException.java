package com.nemo.dong_geul_be.global.exception;

import com.nemo.dong_geul_be.global.response.Code;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{

	private final Code code;

	public BusinessException(Code code) {
		super(code.getMessage());
		this.code = code;
	}
}
