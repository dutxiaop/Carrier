package com.carrier.connect.socket.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SocketHandler {
    void handleRequest(InputStream inputStream, OutputStream outputStream) throws IOException;
}
