package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Launcher {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Please enter your port number in arg parameter: ");
            return;
        }
        int port = Integer.parseInt(args[0]);
        createAndStartServer(port);
    }
    private static void createAndStartServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        ExecutorService executor = Executors.newFixedThreadPool(1);
        server.setExecutor(executor);
        server.createContext("/ping", new PingHandler());
        server.start();
        System.out.println("server is running on port: " + port);
    }
    static class PingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Ack";
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
