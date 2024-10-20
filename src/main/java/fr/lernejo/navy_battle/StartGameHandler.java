package fr.lernejo.navy_battle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;

public class StartGameHandler implements HttpHandler {
    private final int port;
    public StartGameHandler(int port) {
        this.port = port;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            processPostRequest(exchange);
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
    }
    private void processPostRequest(HttpExchange exchange) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, String> requestBody = objectMapper.readValue(exchange.getRequestBody(), Map.class);
            if (isValidRequest(requestBody)) {
                sendSuccessResponse(exchange, objectMapper);
            } else {
                exchange.sendResponseHeaders(400, -1);
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(400, -1);
        }
    }
    private boolean isValidRequest(Map<String, String> requestBody) {
        return requestBody.containsKey("id") && requestBody.containsKey("url") && requestBody.containsKey("message");
    }
    private void sendSuccessResponse(HttpExchange exchange, ObjectMapper objectMapper) throws IOException {
        Map<String, String> responseBody = Map.of(
            "id", UUID.randomUUID().toString(),
            "url", "http://localhost:" + port,
            "message", "je vais te bouffer"
        );
        String responseJson = objectMapper.writeValueAsString(responseBody);
        exchange.sendResponseHeaders(202, responseJson.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseJson.getBytes());
        }
    }
}
