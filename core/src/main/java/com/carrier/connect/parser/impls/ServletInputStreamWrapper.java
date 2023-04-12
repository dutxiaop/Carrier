package com.carrier.connect.parser.impls;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ServletInputStreamWrapper extends ServletInputStream {

    private final InputStream inputStream;

    public ServletInputStreamWrapper(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public boolean isFinished() {
        try {
            return inputStream.available() == 0;
        } catch (IOException e) {
            throw new RuntimeException("Error checking if input stream is finished.", e);
        }
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException("ReadListener is not supported.");
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }
}
