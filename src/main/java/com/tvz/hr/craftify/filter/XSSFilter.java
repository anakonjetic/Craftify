package com.tvz.hr.craftify.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class XSSFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        XSSRequestWrapper wrappedRequest = new XSSRequestWrapper(httpRequest);
        chain.doFilter(wrappedRequest, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
