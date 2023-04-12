package com.carrier.unit.connect;

import com.carrier.connect.socket.interfaces.SocketHandler;
import com.carrier.connect.socket.impls.SocketHandlerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;

class SocketHandlerTest {
    private SocketHandler socketHandler;

    // 注入SocketHandler实现
    @BeforeEach
    void setUp() {
        socketHandler = new SocketHandlerImpl();
    }

    @Test
    void testHandleRequest() throws IOException {
        // Prepare test data
        String request = "GET / HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "User-Agent: TestAgent\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(request.getBytes("UTF-8"));
        OutputStream outputStream = new ByteArrayOutputStream();

        // Call the handleRequest method
        socketHandler.handleRequest(inputStream, outputStream);

        // Verify the output
        String response = outputStream.toString();
        assertTrue(response.startsWith("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Hello, World!"));
    }
}
