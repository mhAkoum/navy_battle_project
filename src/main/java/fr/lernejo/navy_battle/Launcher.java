package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Launcher {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 1) {
            System.err.println("Please enter your port number in arg parameter: ");
            return;
        }
        int port = Integer.parseInt(args[0]);
        createAndStartServer(port);
        if (args.length > 1) {
            String adversaryUrl = args[1];
            new GameClient().sendPostRequest(port, adversaryUrl);
        }
    }

    private static void createAndStartServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        ExecutorService executor = Executors.newFixedThreadPool(1);
        server.setExecutor(executor);
        server.createContext("/ping", new PingHandler());
        server.createContext("/api/game/start", new StartGameHandler(port));
        server.start();
        System.out.println("server is running on port: " + port);
    }

}


