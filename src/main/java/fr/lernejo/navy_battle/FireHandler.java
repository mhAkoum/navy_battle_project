package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;

public class FireHandler implements HttpHandler {
    private final GameState gameState;

    public FireHandler(GameState gameState){
        this.gameState = gameState;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().getQuery();
            String cell = query != null && query.startsWith("cell=") ? query.substring(5) : null;

            if (cell != null) {
                String consequence = gameState.fireAt(cell);
                boolean shipLeft = gameState.shipsLeft();

                Map<String, Object> response = Map.of(
                    "consequence", consequence,
                    "shipLeft", shipLeft
                );

                String jsonResponse = new ObjectMapper().writeValueAsString(response);
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                exchange.sendResponseHeaders(200, jsonResponse.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(jsonResponse.getBytes());
                }

            } else {
                exchange.sendResponseHeaders(400, -1);
            }
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
    }
}
