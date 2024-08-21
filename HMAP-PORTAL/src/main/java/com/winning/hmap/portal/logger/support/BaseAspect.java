package com.winning.hmap.portal.logger.support;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

public class BaseAspect {

    /**
     * 获取入参
     *
     * @param proceedingJoinPoint
     * @return
     */
    public static Map<String,Object> getRequestParamsByProceedingJoinPoint(ProceedingJoinPoint proceedingJoinPoint) {
        //参数名
        String[] paramNames = ((MethodSignature) proceedingJoinPoint.getSignature()).getParameterNames();
        //参数值
        Object[] paramValues = proceedingJoinPoint.getArgs();
        return buildRequestParam(paramNames, paramValues);
    }

    public static Map<String,Object> getRequestParamsByJoinPoint(JoinPoint joinPoint) {
        //参数名
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        //参数值
        Object[] paramValues = joinPoint.getArgs();
        return buildRequestParam(paramNames, paramValues);
    }

    public static Map<String,Object> buildRequestParam(String[] paramNames, Object[] paramValues) {
        try {
            Map<String,Object> requestParams = new HashMap<>();
            for (int i = 0; i < paramNames.length; i++) {
                Object value = paramValues[i];
                //如果是文件对象
                if (value instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) value;
                    //获取文件名
                    value = file.getOriginalFilename();
                } else if (value instanceof MultipartFile[]) {
                    MultipartFile[] files = (MultipartFile[]) value;
                    StringBuilder fileNames = new StringBuilder();
                    for (MultipartFile file : files) {
                        fileNames.append(file.getOriginalFilename()).append(";");
                    }
                    value = fileNames.toString();
                } else if (isJsonString(value.toString())) {
                    value = JSONObject.parseObject(value.toString());
                } else if ("response".equals(paramNames[i]) || "request".equals(paramNames[i])) {
                    continue;
                }
                requestParams.put(paramNames[i], value);
            }
            return requestParams;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isJsonString(String content) {
        if (StringUtils.isEmpty(content)) {
            return false;
        }
        try {
            JSONObject.parse(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
