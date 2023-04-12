package com.carrier.connect.socket.impls;

import com.carrier.connect.parser.impls.CarrierHttpServletResponse;
import com.carrier.connect.parser.interfaces.HttpRequestParser;
import com.carrier.connect.parser.interfaces.RequestDispatcher;
import com.carrier.connect.socket.interfaces.SocketHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DefaultSocketHandler implements SocketHandler {

    private HttpRequestParser httpRequestParser;
    private RequestDispatcher dispatcher;

    public DefaultSocketHandler(HttpRequestParser httpRequestParser, RequestDispatcher dispatcher) {
        this.httpRequestParser = httpRequestParser;
        this.dispatcher = dispatcher;
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream) throws IOException {
        HttpServletRequest request = this.httpRequestParser.parse(inputStream);
        CarrierHttpServletResponse response = new CarrierHttpServletResponse(outputStream);
        try {
            HttpServlet servlet = dispatcher.dispatch(request, response);
            if (servlet != null) {
                servlet.service(request, response);
            } else {
                handleStaticFile(request, response);
            }
            if (!response.isCommitted()) {
                response.flushBuffer();
            }
        } catch (ServletException e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
        }
    }

    private void handleStaticFile(HttpServletRequest request, CarrierHttpServletResponse response) {

    }
}
