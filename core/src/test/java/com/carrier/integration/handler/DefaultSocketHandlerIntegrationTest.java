package com.carrier.integration.handler;

import com.carrier.connect.parser.impls.DefaultHttpRequestParser;
import com.carrier.connect.parser.impls.DefaultRequestDispatcher;
import com.carrier.connect.parser.interfaces.HttpRequestParser;
import com.carrier.connect.parser.interfaces.RequestDispatcher;
import com.carrier.connect.socket.impls.DefaultSocketHandler;
import com.carrier.integration.stub.MockServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServlet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultSocketHandlerIntegrationTest {

    private DefaultSocketHandler defaultSocketHandler;

    private HttpRequestParser httpRequestParser;
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() {
        httpRequestParser = new DefaultHttpRequestParser(); // Replace with your HttpRequestParser implementation
        Map<String, HttpServlet> servletMapping = new HashMap<>();
        servletMapping.put("/test", new MockServlet()); // Register the MockServlet with the dispatcher
        requestDispatcher = new DefaultRequestDispatcher(servletMapping); // Replace with your RequestDispatcher implementation
        defaultSocketHandler = new DefaultSocketHandler(httpRequestParser, requestDispatcher);
    }

    @Test
    void handleGetRequest() throws IOException {
        String requestString = "GET /test?param1=value1&param2=value2 HTTP/1.1\r\nHost: localhost\r\nCookie: testCookie=testValue\r\n\r\n";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        defaultSocketHandler.handleRequest(new ByteArrayInputStream(requestString.getBytes()), outputStream);

        String[] responseParts = outputStream.toString().split("\r\n\r\n");
        String responseHeaders = responseParts[0];
        String responseBody = responseParts[1];

        String expectedHeaders = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nSet-Cookie: testCookie=testValue;";
        String expectedResponseBody = "GET request received with param1: value1, param2: value2, testCookie: testValue";

        assertEquals(expectedHeaders, responseHeaders);
        assertEquals(expectedResponseBody, responseBody.trim());
    }

    @Test
    void handlePostRequest() throws IOException {
        String requestBody = "param1=value1&param2=value2";
        String requestString = "POST /test HTTP/1.1\r\nHost: localhost\r\nContent-Length: " + requestBody.length() + "\r\nContent-Type: application/x-www-form-urlencoded\r\n\r\n" + requestBody;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        defaultSocketHandler.handleRequest(new ByteArrayInputStream(requestString.getBytes()), outputStream);

        String[] responseParts = outputStream.toString().split("\r\n\r\n");
        String responseHeaders = responseParts[0];
        String responseBody = responseParts[1];

        String expectedHeaders = "HTTP/1.1 200 OK\r\nContent-Type: text/plain";
        String expectedResponseBody = "POST request received with param1: value1, param2: value2";

        assertEquals(expectedHeaders, responseHeaders);
        assertEquals(expectedResponseBody, responseBody.trim());
    }
}

