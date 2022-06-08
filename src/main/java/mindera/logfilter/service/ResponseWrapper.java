package mindera.logfilter.service;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ResponseWrapper extends HttpServletResponseWrapper {

    final ByteArrayOutputStream baos;
    final ByteArrayPrintWriter pw;

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
        this.baos = new ByteArrayOutputStream();
        this.pw = new ByteArrayPrintWriter(baos);
    }

    public ByteArrayOutputStream getBaos() {
        return baos;
    }


    @Override
    public PrintWriter getWriter() {
        return pw;
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }

            @Override
            public void write(int b) {
                baos.write(b);
            }
        };
    }

    public static class ByteArrayPrintWriter extends PrintWriter {
        public ByteArrayPrintWriter(OutputStream out) {
            super(out);
        }
    }

}
