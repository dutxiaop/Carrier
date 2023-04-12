package com.carrier.starter;

import com.carrier.connect.observer.impls.ThreadPoolStatusObserver;
import com.carrier.connect.observer.interfaces.ThreadPoolObserver;
import com.carrier.connect.parser.impls.DefaultRequestDispatcher;
import com.carrier.connect.parser.impls.DefaultHttpRequestParser;
import com.carrier.connect.socket.impls.DefaultSocketHandler;
import com.carrier.connect.socket.interfaces.SocketHandler;
import com.carrier.util.NamedThreadFactory;
import com.carrier.util.ThreadPoolFactory;

import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

public class CarrierStarter {
    private static final int PORT = 8088;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            HashMap<String, HttpServlet> servletMapping = new HashMap<>();


            SocketHandler socketHandler = new DefaultSocketHandler(
                    new DefaultHttpRequestParser(),
                    new DefaultRequestDispatcher(servletMapping));

            ExecutorService threadPool = buildSocketHandlerPool();

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    // 设置 Socket 选项
                    clientSocket.setSoTimeout(30000); // 30秒超时
                    clientSocket.setTcpNoDelay(true); // 禁用 Nagle 算法

                    // 提交任务到线程池
                    threadPool.submit(new SocketTask(clientSocket, socketHandler));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ExecutorService buildSocketHandlerPool() {
        NamedThreadFactory namedThreadFactory = new NamedThreadFactory("Worker");
        ThreadPoolObserver threadPoolObserver = new ThreadPoolStatusObserver();
        int poolSize = 10;
        int monitorIntervalSeconds = 5;

        ThreadPoolFactory threadPoolFactory = ThreadPoolFactory.builder()
                .withNamedThreadFactory(namedThreadFactory)
                .withThreadPoolObserver(threadPoolObserver)
                .withPoolSize(poolSize)
                .withMonitorIntervalSeconds(monitorIntervalSeconds)
                .build();

        return threadPoolFactory.createFixedThreadPool();
    }
}
