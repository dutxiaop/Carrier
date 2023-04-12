package com.carrier.connect.socket.impls;

import com.carrier.connect.socket.interfaces.SocketHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SocketHandlerImpl implements SocketHandler {
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));


        // 2.1 读取请求行
        String requestLine = in.readLine();
        String[] requestLineParts = requestLine.split(" ");
        String httpMethod = requestLineParts[0];
        String uri = requestLineParts[1];
        String httpVersion = requestLineParts[2];

        // 2.2 读取请求头
        Map<String, String> headers = new HashMap<>();
        String headerLine;
        while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
            String[] headerParts = headerLine.split(": ");
            headers.put(headerParts[0], headerParts[1]);
        }

        // 2.3 读取请求体
        String requestBody = null;
        if ("POST".equals(httpMethod)) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            char[] buffer = new char[contentLength];
            in.read(buffer, 0, contentLength);
            requestBody = new String(buffer);
        }

        // 处理请求
        // ...

        // 发送响应
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n" +
                "Hello, World!";
        outputStream.write(response.getBytes("UTF-8"));
        outputStream.flush();
    }
}
