package com.carrier.connect.parser.impls;

import com.carrier.connect.parser.interfaces.RequestDispatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class DefaultRequestDispatcher implements RequestDispatcher {
    private final Map<String, HttpServlet> servletMapping;

    public DefaultRequestDispatcher(Map<String, HttpServlet> servletMapping) {
        this.servletMapping = servletMapping;
    }

    // 处理请求的方法
    @Override
    public HttpServlet dispatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 将URI分为两部分：路径和查询参数
        String requestUri = request.getRequestURI();
        int queryParamStart = requestUri.indexOf("?");
        if (queryParamStart > 0) {
            requestUri = requestUri.substring(0, queryParamStart);
        }

        // 查找 Servlet 映射
        return findServlet(requestUri);
    }

    // 查找 Servlet 映射
    private HttpServlet findServlet(String requestUri) {
        return servletMapping.get(requestUri);
    }
}
