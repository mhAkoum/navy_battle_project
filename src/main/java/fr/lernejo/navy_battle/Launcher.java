package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Map;
import java.util.UUID;

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
        server.createContext("/api/game/start", new StartGameHandler(port));
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
    static class StartGameHandler implements HttpHandler {
        private final int port;
        public StartGameHandler(int port) {
            this.port = port;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    Map<String, String> requestBody = objectMapper.readValue(exchange.getRequestBody(), Map.class);
                    if (requestBody.containsKey("id") && requestBody.containsKey("url") && requestBody.containsKey("message")) {
                        Map<String, String> responseBody = Map.of(
                            "id", UUID.randomUUID().toString(),
                            "url", "http://localhost:" + port,
                            "message", "show me your best"
                        );
                        String responseJson = objectMapper.writeValueAsString(responseBody);
                        exchange.sendResponseHeaders(202, responseJson.length());
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(responseJson.getBytes());
                        }
                    } else {
                        exchange.sendResponseHeaders(400, -1);
                    }
                } catch (IOException e) {
                    exchange.sendResponseHeaders(400, -1);
                }
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        }
    }
}

