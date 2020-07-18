package com.auth.framework;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.auth.service.TransactionLogService;
import com.auth.vo.TransactionLog;

@Component
@Aspect
public class LogAspect {
	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

	@Resource
    private TransactionLogService transactionLogService;
	
    @Around("execution(* com.auth.service.TransferService.*(..))")
    public Object logging(ProceedingJoinPoint pjp) throws Throwable {
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
    		    .getRequest();
    			logger.info("{} {} from {}",
    		    request.getMethod(),
    		    request.getRequestURI(),
    		    request.getRemoteAddr());
    	TransactionLog log = TransactionLog.builder()
    			.status('S')
    			.httpMethod(request.getMethod())
    			.uri(request.getRequestURI())
    			.remoteAddr(request.getRemoteAddr())
    			.classPath(pjp.getSignature().getDeclaringTypeName())
    			.method(pjp.getSignature().getName())
    			.build();
    	transactionLogService.insertTransactionLog(log);
        logger.info("[START] " + pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
        Object result = pjp.proceed();
        log = TransactionLog.builder()
    			.status('F')
    			.httpMethod(request.getMethod())
    			.uri(request.getRequestURI())
    			.remoteAddr(request.getRemoteAddr())
    			.classPath(pjp.getSignature().getDeclaringTypeName())
    			.method(pjp.getSignature().getName())
    			.build();
    	transactionLogService.insertTransactionLog(log);
        logger.info("[FINISH]" + pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
        return result;
    }

}
