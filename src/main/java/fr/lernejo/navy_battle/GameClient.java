package fr.lernejo.navy_battle;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
public class GameClient {
    public void sendPostRequest(int myPort, String adversaryUrl) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String jsonRequest = String.format("{\"id\":\"%s\", \"url\":\"http://localhost:%d\", \"message\":\"Let the battle begin!\"}",
            UUID.randomUUID(), myPort);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(adversaryUrl + "/api/game/start"))
            .setHeader("Accept", "application/json")
            .setHeader("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Réponse du serveur : " + response.body());
    }
}
