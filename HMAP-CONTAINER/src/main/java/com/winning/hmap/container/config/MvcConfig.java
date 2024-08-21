package com.winning.hmap.container.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${auth.login.url:/HMAPV5/}")
    private String staticUrl;

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        //当前系统已经默认使用jackson，内部bean已经有相关注解  @RequestBody String json 有问题
//        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
//        List<MediaType> mediaTypes = new ArrayList<>();
//        mediaTypes.add(MediaType.parseMediaType(MediaType.TEXT_PLAIN_VALUE));
//        mediaTypes.add(MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE));
//        mediaTypes.add(MediaType.parseMediaType(MediaType.MULTIPART_FORM_DATA_VALUE));
//        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(mediaTypes);
//        mappingJackson2HttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
//        converters.add(0, mappingJackson2HttpMessageConverter);
//    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        String dir = staticUrl.split("/") [1];
//        registry.addResourceHandler("/"+ dir)
//                .addResourceLocations("file:static/" + dir + "/");
//        registry.addResourceHandler("/"+ dir + "/**")
//                .addResourceLocations("file:static/" + dir + "/");
//    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(staticUrl).setViewName("forward:/" + staticUrl +"/index.html");
    }
}
