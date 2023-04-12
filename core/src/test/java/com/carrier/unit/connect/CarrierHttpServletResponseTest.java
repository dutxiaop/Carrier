package com.carrier.unit.connect;

import com.carrier.connect.parser.impls.CarrierHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.Cookie;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarrierHttpServletResponseTest {
    private CarrierHttpServletResponse response;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        response = new CarrierHttpServletResponse(outputStream);
    }

    @Test
    void testFlushHeadersWithoutContentTypeAndContentLength() throws IOException {
        response.flushHeaders();
        String result = outputStream.toString(StandardCharsets.UTF_8.name());

        assertTrue(result.contains("HTTP/1.1 200 OK\r\n"));
        assertTrue(result.contains("Content-Type: \r\n"));
    }

    @Test
    void testFlushHeadersWithContentTypeAndContentLength() throws IOException {
        response.setContentType("text/html");
        response.setContentLength(42);
        response.flushHeaders();
        String result = outputStream.toString(StandardCharsets.UTF_8.name());

        assertTrue(result.contains("HTTP/1.1 200 OK\r\n"));
        assertTrue(result.contains("Content-Type: text/html\r\n"));
        assertTrue(result.contains("Content-Length: 42\r\n"));
    }

    @Test
    void testFlushHeadersWithCookies() throws IOException {
        Cookie cookie = new Cookie("testCookie", "testValue");
        cookie.setPath("/test");
        cookie.setDomain("example.com");
        cookie.setMaxAge(3600);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
        response.flushHeaders();
        String result = outputStream.toString(StandardCharsets.UTF_8.name());

        assertTrue(result.contains("Set-Cookie: testCookie=testValue;Path=/test;Domain=example.com;Expires="));
        assertTrue(result.contains(";Secure;HttpOnly;\r\n"));
    }

    @Test
    void testFlushHeadersWhenAlreadyFlushed() throws IOException {
        response.flushHeaders();
        String result1 = outputStream.toString(StandardCharsets.UTF_8.name());

        outputStream.reset();
        response.flushHeaders();
        String result2 = outputStream.toString(StandardCharsets.UTF_8.name());

        assertEquals("", result2);
    }
}

