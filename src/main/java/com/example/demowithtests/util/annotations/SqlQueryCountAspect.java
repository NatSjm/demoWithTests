package com.example.demowithtests.util.annotations;

import com.example.demowithtests.util.HibernateQueryInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static com.example.demowithtests.util.annotations.LogColorConstants.ANSI_BLUE;
import static com.example.demowithtests.util.annotations.LogColorConstants.ANSI_RESET;

@Slf4j
@Aspect
@Component
public class SqlQueryCountAspect {

    @Pointcut("execution(public * com.example.demowithtests.service.fillDataBase.LoaderServiceBean.*(..))")
    public void callAtLoaderServicePublicMethods() {}

    @Before("callAtLoaderServicePublicMethods()")
    public void logBefore() {
        HibernateQueryInterceptor.resetQueryCount();
    }

    @AfterReturning(value = "callAtLoaderServicePublicMethods()")
    public void logAfter(JoinPoint joinPoint) {
        int queryCount = HibernateQueryInterceptor.getQueryCount();
        String methodName = joinPoint.getSignature().toShortString();
        log.debug(ANSI_BLUE + "Service: {} - Number of SQL queries executed: {}"  + ANSI_RESET, methodName, queryCount);
    }
}