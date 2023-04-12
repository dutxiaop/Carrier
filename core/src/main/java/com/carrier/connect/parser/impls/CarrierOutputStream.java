package com.carrier.connect.parser.impls;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.OutputStream;

public class CarrierOutputStream extends ServletOutputStream {

    private CarrierHttpServletResponse response;
    private OutputStream outputStream;

    public CarrierOutputStream(CarrierHttpServletResponse response, OutputStream outputStream) {
        this.response = response;
        this.outputStream = outputStream;
    }

    @Override
    public void write(int b) throws IOException {
        response.flushHeaders();
        outputStream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        response.flushHeaders();
        outputStream.write(b, off, len);
    }

    @Override
    public boolean isReady() {
        throw new UnsupportedOperationException("Async I/O not supported for CarrierOutputStream");
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        throw new UnsupportedOperationException("Async I/O not supported for CarrierOutputStream");
    }
}
