package com.carrier.connect.parser.impls;

import java.io.OutputStream;
import java.io.PrintWriter;

public class CarrierPrintWriter extends PrintWriter {

    private CarrierHttpServletResponse response;

    public CarrierPrintWriter(OutputStream out, CarrierHttpServletResponse response) {
        super(out);
        this.response = response;
    }

    @Override
    public void write(int c) {
        response.flushHeaders();
        super.write(c);
    }

    @Override
    public void write(char[] buf, int off, int len) {
        response.flushHeaders();
        super.write(buf, off, len);
    }

    @Override
    public void write(String s, int off, int len) {
        response.flushHeaders();
        super.write(s, off, len);
    }
}
