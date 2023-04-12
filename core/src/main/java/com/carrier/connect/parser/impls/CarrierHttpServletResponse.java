package com.carrier.connect.parser.impls;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class CarrierHttpServletResponse implements HttpServletResponse {
    private int statusCode = 200;
    private String statusMessage = "OK";
    private String contentType;
    private OutputStream outputStream;
    private String characterEncoding;
    private long contentLength = -1;
    private Map<String, String> headers = new HashMap<>();
    private List<Cookie> cookies = new ArrayList<>();
    private boolean headerFlushed = false;
    private boolean committed = false;
    private PrintWriter printWriter;
    private ServletOutputStream servletOutputStream;


    public CarrierHttpServletResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.servletOutputStream = new  CarrierOutputStream(this, outputStream);
        this.printWriter = new CarrierPrintWriter(this.outputStream, this);
    }

    @Override
    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    @Override
    public boolean containsHeader(String name) {
        return headers.containsKey(name);
    }

    @Override
    public String encodeURL(String url) {
        return url; // No encoding for this implementation.
    }

    @Override
    public String encodeRedirectURL(String url) {
        return encodeURL(url);
    }

    @Override
    public String encodeUrl(String url) {
        return encodeURL(url);
    }

    @Override
    public String encodeRedirectUrl(String url) {
        return encodeRedirectURL(url);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        // Implementation for sending error response.
    }

    @Override
    public void sendError(int sc) throws IOException {
        // Implementation for sending error response.
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        // Implementation for sending redirect response.
    }

    @Override
    public void setDateHeader(String name, long date) {
        headers.put(name, String.valueOf(date));
    }

    @Override
    public void addDateHeader(String name, long date) {
        setDateHeader(name, date);
    }

    @Override
    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
        setHeader(name, value);
    }

    @Override
    public void setIntHeader(String name, int value) {
        headers.put(name, String.valueOf(value));
    }

    @Override
    public void addIntHeader(String name, int value) {
        setIntHeader(name, value);
    }

    @Override
    public void setStatus(int sc) {
        this.statusCode = sc;
    }

    @Override
    public void setStatus(int sc, String sm) {
        setStatus(sc);
        this.statusMessage = sm;
    }

    @Override
    public int getStatus() {
        return this.statusCode;
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return headers.values();
    }

    @Override
    public Collection<String> getHeaderNames() {
        return headers.keySet();
    }

    @Override
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return this.servletOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return this.printWriter;
    }

    @Override
    public void setCharacterEncoding(String charset) {
        this.characterEncoding = charset;
    }

    @Override
    public void setContentLength(int len) {
        this.contentLength = len;
    }

    @Override
    public void setContentLengthLong(long len) {
        this.contentLength = len;
    }

    @Override
    public void setContentType(String type) {
        this.contentType = type;
    }

    @Override
    public void setBufferSize(int size) {
        // No implementation for this demo.
    }

    @Override
    public int getBufferSize() {
        return 0; // No buffer used in this implementation.
    }

    @Override
    public void flushBuffer() throws IOException {
        flushHeaders(); // 确保在提交响应之前将响应头发送
        getWriter().flush();
        getOutputStream().flush();
        outputStream.flush();
        committed = true;
    }

    @Override
    public void resetBuffer() {
        // No implementation for this demo.
    }

    @Override
    public boolean isCommitted() {
        return committed; // No implementation for this demo.
    }

    @Override
    public void reset() {
        // No implementation for this demo.
    }

    @Override
    public void setLocale(Locale loc) {
        // No implementation for this demo.
    }

    @Override
    public Locale getLocale() {
        return Locale.getDefault();
    }

    public void flushHeaders() {
        if (headerFlushed) {
            return;
        }
        headerFlushed = true;
        PrintWriter writer = new PrintWriter(outputStream);

        // Write status line
        writer.print("HTTP/1.1 ");
        writer.print(statusCode);
        writer.print(" ");
        writer.print(statusMessage);
        writer.print("\r\n");

        // 将content-type和content-length添加到响应头中（如果它们尚未设置）
        headers.putIfAbsent("Content-Type", contentType == null ? "" : contentType);
        if (contentLength != -1) {
            headers.putIfAbsent("Content-Length", String.valueOf(contentLength));
        }

        // Write headers
        for (Map.Entry<String, String> header : headers.entrySet()) {
            writer.print(header.getKey());
            writer.print(": ");
            writer.print(header.getValue());
            writer.print("\r\n");
        }

        // Write cookies
        for (Cookie cookie : cookies) {
            writer.print("Set-Cookie: ");
            writer.print(cookie.getName());
            writer.print("=");
            writer.print(cookie.getValue());
            writer.print(";");

            if (cookie.getPath() != null) {
                writer.print("Path=");
                writer.print(cookie.getPath());
                writer.print(";");
            }

            if (cookie.getDomain() != null) {
                writer.print("Domain=");
                writer.print(cookie.getDomain());
                writer.print(";");
            }

            if (cookie.getMaxAge() != -1) {
                Date expires = new Date(System.currentTimeMillis() + cookie.getMaxAge() * 1000);
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

                writer.print("Expires=");
                writer.print(sdf.format(expires));
                writer.print(";");
            }

            if (cookie.getSecure()) {
                writer.print("Secure;");
            }

            if (cookie.isHttpOnly()) {
                writer.print("HttpOnly;");
            }

            writer.print("\r\n");
        }

        // End of headers
        writer.print("\r\n");
        writer.flush();
    }
}

