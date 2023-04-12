package com.carrier.integration.stub;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MockServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String param1 = req.getParameter("param1");
        String param2 = req.getParameter("param2");

        Cookie cookie = new Cookie("testCookie", "testValue");
        resp.addCookie(cookie);

        String cookieValue = getCookieValue(req, "testCookie");

        resp.setContentType("text/plain");
        resp.getWriter().print("GET request received with param1: " + param1 + ", param2: " + param2 + ", testCookie: " + cookieValue);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String param1 = req.getParameter("param1");
        String param2 = req.getParameter("param2");
        resp.setContentType("text/plain");
        resp.getWriter().print("POST request received with param1: " + param1 + ", param2: " + param2);
    }

    private String getCookieValue(HttpServletRequest req, String cookieName) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

