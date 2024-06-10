package com.tvz.hr.craftify.filter;

import com.tvz.hr.craftify.filter.LoggingFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

public class LoggingFilterTest {

    @Test
    public void testDoFilter() throws ServletException, IOException {
        LoggingFilter loggingFilter = new LoggingFilter();
        ServletRequest request = Mockito.mock(HttpServletRequest.class);
        ServletResponse response = Mockito.mock(ServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);

        loggingFilter.doFilter(request, response, chain);

        Mockito.verify(chain, times(1)).doFilter(any(), any());
    }

    @Test
    public void testLogMessage() {
        // Given
        Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getMethod()).thenReturn("GET");
        Mockito.when(request.getRequestURI()).thenReturn("/example");
        Mockito.when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        logger.info("Request Method: {}, Request URI: {}, Remote Address: {}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());

    }

    @Test
    public void testInit() throws ServletException {
        LoggingFilter loggingFilter = new LoggingFilter();
        FilterConfig filterConfig = Mockito.mock(FilterConfig.class);
        loggingFilter.init(filterConfig);

    }

    @Test
    public void testDestroy() {
        LoggingFilter loggingFilter = new LoggingFilter();

        loggingFilter.destroy();
    }
}
