package com.tvz.hr.craftify.filter;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class XSSRequestWrapperTest {

    @Test
    public void getParameter_ShouldSanitizeInput() {
        String input = "<script>alert('XSS attack');</script>";
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setParameter("param", input);
        XSSRequestWrapper wrapper = new XSSRequestWrapper(mockRequest);

        String sanitizedValue = wrapper.getParameter("param");

        assertEquals("&lt;script&gt;alert('XSS attack');&lt;/script&gt;", sanitizedValue);
    }

    @Test
    public void getParameterValues_ShouldSanitizeInputArray() {
        String[] inputValues = {"<script>alert('XSS attack');</script>", "normal value"};
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addParameter("param", inputValues);
        XSSRequestWrapper wrapper = new XSSRequestWrapper(mockRequest);

        String[] sanitizedValues = wrapper.getParameterValues("param");

        assertEquals("&lt;script&gt;alert('XSS attack');&lt;/script&gt;", sanitizedValues[0]);
        assertEquals("normal value", sanitizedValues[1]);
    }

    @Test
    public void getParameterMap_ShouldSanitizeInput() {
        Map<String, String[]> paramMap = new HashMap<>();
        paramMap.put("param1", new String[]{"<script>alert('XSS attack');</script>"});
        paramMap.put("param2", new String[]{"normal value", "<script>alert('XSS attack');</script>"});
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setParameters(paramMap);
        XSSRequestWrapper wrapper = new XSSRequestWrapper(mockRequest);

        Map<String, String[]> sanitizedMap = wrapper.getParameterMap();

        assertEquals("&lt;script&gt;alert('XSS attack');&lt;/script&gt;", sanitizedMap.get("param1")[0]);
        assertArrayEquals(new String[]{"normal value", "&lt;script&gt;alert('XSS attack');&lt;/script&gt;"}, sanitizedMap.get("param2"));
    }

}
