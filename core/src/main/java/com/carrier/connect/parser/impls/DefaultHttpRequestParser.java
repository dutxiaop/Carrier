package com.carrier.connect.parser.impls;

import com.carrier.connect.parser.interfaces.HttpRequestParser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultHttpRequestParser implements HttpRequestParser {

    @Override
    public HttpServletRequest parse(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String requestLine = in.readLine();
        String[] requestLineParts = requestLine.split(" ");

        String method = requestLineParts[0];
        String uri = requestLineParts[1];
        String httpVersion = requestLineParts[2];

        Map<String, String> headers = new HashMap<>();
        String headerLine;
        while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
            String[] headerParts = headerLine.split(":\\s", 2);
            headers.put(headerParts[0], headerParts[1]);
        }

        Map<String, String[]> parameters = new HashMap<>();
        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            char[] buffer = new char[contentLength];
            in.read(buffer, 0, contentLength);
            String body = new String(buffer);
            String[] pairs = body.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8.name());
                String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name()) : null;
                parameters.put(key, new String[]{value});
            }
        } else {
            String queryString = uri.contains("?") ? uri.substring(uri.indexOf("?") + 1) : "";
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8.name());
                String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name()) : null;
                parameters.put(key, new String[]{value});
            }
        }
        List<Cookie> cookies = new ArrayList<>();
        String cookieHeader = headers.get("Cookie");
        if (cookieHeader != null) {
            String[] cookiePairs = cookieHeader.split(";\\s*");
            for (String cookiePair : cookiePairs) {
                String[] keyValue = cookiePair.split("=", 2);
                String key = keyValue[0].trim();
                String value = keyValue.length > 1 ? keyValue[1].trim() : null;
                cookies.add(new Cookie(key, value));
            }
        }

        return new CarrierHttpServletRequest.Builder()
                .method(method)
                .requestURI(uri)
                .headers(headers)
                .inputStream(inputStream)
                .parameters(parameters)
                .cookies(cookies)
                .build();
    }

}
