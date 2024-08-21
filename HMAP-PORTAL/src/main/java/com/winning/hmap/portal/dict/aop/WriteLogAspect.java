package com.winning.hmap.portal.dict.aop;

import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.auth.dto.auth.resp.UserRole;
import com.winning.hmap.portal.dict.dto.req.put.AddLogParam;
import com.winning.hmap.portal.dict.service.LogService;
import lombok.extern.slf4j.Slf4j;
import me.about.widget.spring.mvc.security.SessionUserContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 统一写业务操作日志
 *
 * @author: hugo.zxh
 * @date: 2022/06/14 15:34
 * @description:
 */
@Slf4j
@Aspect
@Component
public class WriteLogAspect {

    @Resource
    private LogService logService;

    @Pointcut("@annotation(com.winning.hmap.portal.dict.aop.WriteLog)")
    public void writeLog() {
    }

    @Around("writeLog()")
    public Object doWriteLog(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        WriteLog writeLog = method.getAnnotation(WriteLog.class);
        //方法签名信息
        Object[] args = joinPoint.getArgs();
        //先执行业务逻辑
        Object proceed = joinPoint.proceed(args);
        LoginUser loginUser = (LoginUser) SessionUserContext.getSessionUser();
        if (loginUser != null) {
            UserRole loginRole = loginUser.getLoginRole();
            //方法注解信息
            String type = writeLog.type();
            String description = writeLog.description() + "【" + (loginRole == null ? "无" : loginRole.getRoleName()) + "】";
            AddLogParam addLogParam = new AddLogParam();
            addLogParam.setOpnType(type);
            addLogParam.setContent(description);
            addLogParam.setIp(getIpAddress());
            logService.add(addLogParam, loginUser);
        }
        return proceed;
    }

    public String getIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
