package com.winning.hmap.container.spring;

import com.alibaba.fastjson.JSON;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.auth.service.license.LicenseCreator;
import com.winning.hmap.portal.auth.service.license.LicenseCreatorConfig;
import lombok.extern.slf4j.Slf4j;
import me.about.widget.spring.mvc.result.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class AuthFilter implements Filter {

    @Value("${mock.user.enable:false}")
    private boolean mockUserEnable;

    @Resource
    private LicenseCreatorConfig licenseCreatorConfig;

    private final PathMatcher antMatcher = new AntPathMatcher();

    @Value("#{'${whitelist.path.patterns:/hmap/license/**}'.split(',')}")
    private List<String> whitelistPathPatterns;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest =(HttpServletRequest) request;
        HttpServletResponse httpResponse =(HttpServletResponse) response;
        log.info("url:{}",httpRequest.getRequestURI());
        if(!checkLicense(httpRequest,httpResponse)) {
            return;
        }
        LoginUser loginUser = (LoginUser) httpRequest.getSession().getAttribute("SessionUser");
        if(mockUserEnable && loginUser == null) {
            httpRequest.getSession().setAttribute("SessionUser",SessionUserUtils.mockLoginUser());
        }
        filterChain.doFilter(request,response);
    }

    private boolean checkLicense(HttpServletRequest httpRequest,HttpServletResponse httpResponse) throws IOException {
        LicenseCreator licenseCreator = new LicenseCreator(licenseCreatorConfig);

        boolean b = whitelistPathPatterns.stream()
                .anyMatch(pattern -> antMatcher.match(pattern, httpRequest.getRequestURI()));

        if(!b && !licenseCreator.verify()) {
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json; charset=utf-8");
            PrintWriter out = httpResponse.getWriter();
            out.append(JSON.toJSONString(Result.failed(406, "没有授权证书，请联系管理员")));
            out.flush();
            out.close();
            return false;
        }
        return true;
    }
}
