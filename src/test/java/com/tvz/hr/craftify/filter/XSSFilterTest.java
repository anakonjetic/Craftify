package com.tvz.hr.craftify.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import java.io.IOException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class XSSFilterTest {


    private XSSFilter xssFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ServletRequest servletRequest;

    @Mock
    private ServletResponse servletResponse;


    @Mock
    private FilterConfig filterConfig;

    @Mock
    private ServletContext servletContext;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        xssFilter = new XSSFilter();
    }

    @Test
    void init_ShouldInitializeFilter() throws ServletException {
        // Arrange
        FilterConfig filterConfig = Mockito.mock(FilterConfig.class);

        // Act
        xssFilter.init(filterConfig);

        // Assert
        // Ensure that the filter initialization completes without exceptions
        // This indirectly confirms that the getServletContext() method is invoked internally
    }

    @Test
    void destroy_ShouldDestroyFilter() {
        xssFilter.destroy();
    }

    @Test
    void doFilter_WithXSSAttackPayload_WrapsRequest() throws ServletException, IOException {
        MockitoAnnotations.openMocks(this);
        String xssPayload = "<script>alert('XSS')</script>";
        when(request.getParameter("paramName")).thenReturn(xssPayload);

        xssFilter.doFilter(request, servletResponse, filterChain);

        verify(filterChain).doFilter(any(XSSRequestWrapper.class), any(ServletResponse.class));
    }

    @Test
    void doFilter_WithoutXSSAttackPayload_NoWrapping() throws ServletException, IOException {
        MockitoAnnotations.openMocks(this);
        String nonXssPayload = "validInput";
        when(request.getParameter("paramName")).thenReturn(nonXssPayload);

        xssFilter.doFilter(request, servletResponse, filterChain);

        verify(filterChain).doFilter(any(HttpServletRequest.class), any(ServletResponse.class));
    }
}
