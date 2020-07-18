package com.auth.util;

import java.net.BindException;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth.exception.BizException;
import com.auth.vo.ErrorCodes;
import com.auth.vo.ResultResponse;


@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 비즈니스 에러 로직 실패시 BizExcpeiton 발생
	 */
	@ExceptionHandler(BizException.class)
	protected ResultResponse handleBizException(BizException e) {
		final ResultResponse response = ResultResponse.builder().errCode(e.getErrorCode()).errMsg(e.getMessage()).build();
		return response;
	}
	
    /**
     *  javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     *  HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     *  주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResultResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    	final ResultResponse response = ResultResponse.builder().errCode(ErrorCodes.E9).errMsg(e.getMessage()).build();
        return response;
    }

    /**
     * @ModelAttribut 으로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @ExceptionHandler(BindException.class)
    protected ResultResponse handleBindException(BindException e) {
    	final ResultResponse response = ResultResponse.builder().errCode(ErrorCodes.E9).errMsg(e.getMessage()).build();
        return response;
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResultResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
    	final ResultResponse response = ResultResponse.builder().errCode(ErrorCodes.E9).errMsg(e.getMessage()).build();
        return response;
    }
    
    @ExceptionHandler(Exception.class)
    protected ResultResponse handleException(Exception e) {
        final ResultResponse response = ResultResponse.builder().errCode(ErrorCodes.E9).errMsg(e.getMessage()).build();
        return response;
    }
}