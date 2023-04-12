package com.carrier.starter;

import com.carrier.connect.socket.interfaces.SocketHandler;

import java.io.IOException;
import java.net.Socket;

public class SocketTask implements Runnable {
    private final Socket clientSocket;
    private final SocketHandler socketHandler;

    public SocketTask(Socket clientSocket, SocketHandler socketHandler) {
        this.clientSocket = clientSocket;
        this.socketHandler = socketHandler;
    }

    @Override
    public void run() {
        try {
            // 解析请求
            socketHandler.handleRequest(clientSocket.getInputStream(), clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}