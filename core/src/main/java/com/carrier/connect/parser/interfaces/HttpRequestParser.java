package com.carrier.connect.parser.interfaces;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.IOException;

public interface HttpRequestParser {
    HttpServletRequest parse(InputStream inputStream) throws IOException;
}
