package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class ObtemAcessToken {
  public static void main(String[] args) throws Exception {
    String code = "4/0AVMBsJii8mal7hpJdelvDE-GiYfsv5qf3vMxH4rdMxq0W5-G-1I1TSCtn5jwmsQ4TkBJrQ";
    String clientId = "317747790200-993n1jftmuvomqobbade0tfu82fqgcf9.apps.googleusercontent.com";
    String clientSecret = "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";
    String redirectUri = "http://localhost:8080/oauth2callback";

    Map<String, String> params = Map.of(
            "code", code,
            "client_id", clientId,
            "client_secret", clientSecret,
            "redirect_uri", redirectUri,
            "grant_type", "authorization_code"
    );
    String form = params.entrySet().stream()
            .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
            .collect(Collectors.joining("&"));

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://oauth2.googleapis.com/token"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(form))
            .build();

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println("Resposta do Google (tokens):");
    System.out.println(response.body());
    // O resultado será um JSON com "access_token", "refresh_token", etc.
  }
}
