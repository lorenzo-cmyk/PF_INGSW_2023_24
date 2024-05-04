package it.polimi.ingsw.am32.network;

import it.polimi.ingsw.am32.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SKClientAcceptor implements Runnable {
    private final int port;

    public SKClientAcceptor(int port) {
        this.port = port;
    }

    public void run() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }

        System.out.println("Server ready");

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new SKServerNode(socket));
            } catch (IOException e) {
                break;
            }
        }
    }
}
