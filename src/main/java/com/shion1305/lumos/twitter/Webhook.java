package com.shion1305.lumos.twitter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/twitter")
public class Webhook extends HttpServlet {
    private static final Logger logger=Logger.getLogger("TwitterWebhook");
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        logger.info(httpServletRequestToString(req));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info(httpServletRequestToString(req));
    }

    private String httpServletRequestToString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("Request Method = [" + request.getMethod() + "], ");
        sb.append("Request URL Path = [" + request.getRequestURL() + "], ");
        String headers =
                Collections.list(request.getHeaderNames()).stream()
                        .map(headerName -> headerName + " : " + Collections.list(request.getHeaders(headerName)))
                        .collect(Collectors.joining(", "));

        if (headers.isEmpty()) {
            sb.append("Request headers: NONE,");
        } else {
            sb.append("Request headers: [" + headers + "],");
        }
        String parameters =
                Collections.list(request.getParameterNames()).stream()
                        .map(p -> p + " : " + Arrays.asList(request.getParameterValues(p)))
                        .collect(Collectors.joining(", "));

        if (parameters.isEmpty()) {
            sb.append("Request parameters: NONE.");
        } else {
            sb.append("Request parameters: [" + parameters + "].");
        }

        return sb.toString();
    }
}
