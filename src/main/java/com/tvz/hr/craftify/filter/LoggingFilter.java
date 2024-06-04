package com.tvz.hr.craftify.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*")
public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String method = httpRequest.getMethod();
        String uri = httpRequest.getRequestURI();
        String remoteAddr = httpRequest.getRemoteAddr();
        logger.info(org.slf4j.MarkerFactory.getMarker("API_REQUEST"), "Request Method: {}, Request URI: {}, Remote Address: {}", method, uri, remoteAddr);
        System.out.println("Request Method: " + method + ", Request URI: " + uri + ", Remote Address: " + remoteAddr);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
