package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class EnviaEmail {
  public static void main(String[] args) throws Exception {
    String accessToken = "ya29.A0AS3H6NyvxQeALZj7J8J5NbcZzwreqHo1PZpAjrVJNOryl6dX8EZhCIQl4qiYWOB9OsoDGedvUuuuIgDlMGQ4I0C65v-vrs1ybaa21RoR3SrPZJzcWEcg1Bv6Vyk15OQh6S6tardj1t-9PtnYXfX1j7qHHiKWf67iX8_Bbmi32aA4Cz7lQBkSxfJLLJAztxk9KeBwnswaCgYKAdESAQ8SFQHGX2MiLodWGE5K3a3IFj2mworgpg0206";  // seu token aqui
    String rawEmail = buildRawEmail(
            "mirandaccomp@gmail.com",
            "felipeml1000@gmail.com",
            "Teste via Gmail API",
            "Olá! Email enviado por Java + Gmail API."
    );

    String apiUrl = "https://gmail.googleapis.com/gmail/v1/users/me/messages/send";

    String jsonBody = "{ \"raw\": \"" + rawEmail + "\" }";

    HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(apiUrl))
            .header("Authorization", "Bearer " + accessToken)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println("Resposta do envio:");
    System.out.println(response.body());
  }

  // Monta o e-mail no formato RFC 2822, base64url-encoded
  public static String buildRawEmail(String from, String to, String subject, String body) {
    String email = "From: " + from + "\r\n"
            + "To: " + to + "\r\n"
            + "Subject: " + subject + "\r\n"
            + "\r\n"
            + body;
    return Base64.getUrlEncoder().withoutPadding().encodeToString(email.getBytes());
  }
}
