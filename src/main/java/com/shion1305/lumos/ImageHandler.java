package com.shion1305.lumos;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;

@WebServlet("/image/*")
public class ImageHandler extends HttpServlet implements ServletContextListener {
    private static HashMap<String, ByteBuffer> data;
    private static ByteBuffer byteBuffer;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println(req.getPathInfo());
        resp.setContentType("image/png");
        resp.getOutputStream().write(byteBuffer.array());
        resp.getOutputStream().close();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        data = new HashMap<>();
        try {
            FileInputStream stream = new FileInputStream(sce.getServletContext().getRealPath("next.png"));
            byteBuffer = ByteBuffer.wrap(stream.readAllBytes());
        } catch (IOException e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }
    }
}
