package com.tvz.hr.craftify.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.text.StringEscapeUtils;

import java.util.HashMap;
import java.util.Map;

public class XSSRequestWrapper extends HttpServletRequestWrapper {
    public XSSRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return sanitize(value);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }

        int length = values.length;
        String[] sanitizedValues = new String[length];
        for (int i = 0; i < length; i++) {
            sanitizedValues[i] = sanitize(values[i]);
        }
        return sanitizedValues;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = super.getParameterMap();
        Map<String, String[]> sanitizedMap = new HashMap<>();
        for (String key : map.keySet()) {
            sanitizedMap.put(key, sanitize(map.get(key)));
        }
        return sanitizedMap;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return sanitize(value);
    }

    private String sanitize(String value) {
        return value != null ? StringEscapeUtils.escapeHtml4(value) : null;
    }

    private String[] sanitize(String[] values) {
        if (values == null) {
            return null;
        }

        int length = values.length;
        String[] sanitizedValues = new String[length];
        for (int i = 0; i < length; i++) {
            sanitizedValues[i] = sanitize(values[i]);
        }
        return sanitizedValues;
    }
}
