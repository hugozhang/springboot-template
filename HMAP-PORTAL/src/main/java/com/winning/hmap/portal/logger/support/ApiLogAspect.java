package com.winning.hmap.portal.logger.support;

import com.alibaba.fastjson.JSON;
import com.winning.hmap.portal.logger.dto.ApiLogRequest;
import com.winning.hmap.portal.logger.dto.RequestErrorInfo;
import com.winning.hmap.portal.logger.dto.RequestInfo;
import com.winning.hmap.portal.logger.constant.Constant;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * ApiLogAspect
 * @author cpj
 * @version 1.0
 * @description 接口请求拦截打印日志
 */
@Aspect
@Component
public class ApiLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiLogAspect.class);

    @Value("${logger.apilog.flag:1}")
    private String apiLogFlag;

    @Around(value = "@annotation(com.winning.hmap.portal.logger.dto.ApiLogRequest)")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        Object result = proceedingJoinPoint.proceed();
        RequestInfo requestInfo = new RequestInfo();
        // 打印请求的 IP
        requestInfo.setIp(request.getRemoteAddr());
        // 打印请求 url
        requestInfo.setUrl(request.getRequestURL().toString());
        // 打印 Http method
        requestInfo.setHttpMethod(request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        requestInfo.setClassMethod(String.format("%s.%s", proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                proceedingJoinPoint.getSignature().getName()));
        // 打印请求入参
        requestInfo.setRequestParams(BaseAspect.getRequestParamsByProceedingJoinPoint(proceedingJoinPoint));
        // 打印出参
        requestInfo.setResult(result);
        // 执行耗时 ms
        requestInfo.setTimeCost((System.currentTimeMillis() - start) + "ms");
        ApiLogRequest apiLogRequest =  getAspectLogDescription(proceedingJoinPoint);
        if(Constant.FLAG_Y.equals(apiLogFlag)){
            LOGGER.info("调用院端API 成功-【{}】Request Info : {}",apiLogRequest.interfaceName(), JSON.toJSONString(requestInfo));
        }
        return result;
    }

    @AfterThrowing(pointcut = "@annotation(com.winning.hmap.portal.logger.dto.ApiLogRequest)", throwing = "e")
    public void doAfterThrow(JoinPoint joinPoint, RuntimeException e) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        RequestErrorInfo requestErrorInfo = new RequestErrorInfo();
        requestErrorInfo.setIp(request.getRemoteAddr());
        requestErrorInfo.setUrl(request.getRequestURL().toString());
        requestErrorInfo.setHttpMethod(request.getMethod());
        requestErrorInfo.setClassMethod(String.format("%s.%s", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName()));
        requestErrorInfo.setRequestParams(BaseAspect.getRequestParamsByJoinPoint(joinPoint));
        requestErrorInfo.setException(e);
        ApiLogRequest apiLogRequest = getAspectLogDescription(joinPoint);
        if (Constant.FLAG_Y.equals(apiLogFlag)) {
            LOGGER.error("调用院端API 失败-【{}】Error Request Info : {}", apiLogRequest.interfaceName(), JSON.toJSONString(requestErrorInfo));
        }
    }

    /**
     * 获取切面注解的实体
     *
     * @param joinPoint 切点
     * @return 描述信息
     * @throws Exception
     */
    public ApiLogRequest getAspectLogDescription(JoinPoint joinPoint)
            throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = null;
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        methodSignature = (MethodSignature) signature;
        Method currentMethod =  joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        return currentMethod.getAnnotation(ApiLogRequest.class);
    }

}