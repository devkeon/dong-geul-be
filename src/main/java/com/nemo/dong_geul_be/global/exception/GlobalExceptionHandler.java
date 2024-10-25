package com.nemo.dong_geul_be.global.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nemo.dong_geul_be.global.response.Code;
import com.nemo.dong_geul_be.global.response.Response;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = BusinessException.class)
	public Response<Void> businessExceptionHandler(BusinessException businessException) {

		log.error("BusinessException: {}", businessException.getCode());
		log.error("error: ", businessException);

		return Response.fail(businessException.getCode());
	}

	@ExceptionHandler(value = MessagingException.class)
	public Response<Void> messagingExceptionHandler(MessagingException messagingException) {

		log.error("error: ", messagingException);

		return Response.fail(Code.MAIL_SERVER_ERROR);
	}

}
