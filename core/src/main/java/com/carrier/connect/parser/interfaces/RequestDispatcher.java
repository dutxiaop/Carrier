package com.carrier.connect.parser.interfaces;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface RequestDispatcher {
    HttpServlet dispatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
