package com.winning.hmap.container.config;

import org.springframework.core.annotation.Order;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(SwaggerPluginSupport.OAS_PLUGIN_ORDER)
//@Component
public class CustomParameterFilter implements OperationBuilderPlugin {

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    @Override
    public void apply(OperationContext context) {

        Set<RequestParameter> requestParameters = context.operationBuilder().build().getRequestParameters();

        Set<RequestParameter> parameterSet = requestParameters.stream()
                .filter(parameter -> parameter.getIn().equals(ParameterType.BODY)) // 假设LoginUser参数的描述是"loginUser"
                .collect(Collectors.toSet());
        context.operationBuilder().requestParameters(parameterSet);

    }
}
