package org.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

public class Main {

  public static void main(String[] args) throws Exception {

    iniciaServidorCallBack();

    solicitaPermissao();

    System.out.println("Aguardando resposta do Google...");
  }

  private static void solicitaPermissao() throws IOException, URISyntaxException {
    String idAppGoogle = "317747790200-993n1jftmuvomqobbade0tfu82fqgcf9.apps.googleusercontent.com";
    String urlDeCallBack = "http://localhost:" + 8080 + "/callBack";
    String url = "https://accounts.google.com/o/oauth2/v2/auth"
            + "?client_id=" + idAppGoogle
            + "&redirect_uri=" + urlDeCallBack
            + "&response_type=code"
            + "&scope=https://mail.google.com/"
            + "&access_type=offline"
            + "&prompt=consent";

    if (Desktop.isDesktopSupported()) {
      Desktop.getDesktop().browse(new URI(url));
      System.out.println("Aberto navegador para: " + url);
    } else {
      System.out.println("Abra manualmente o navegador nessa URL:\n" + url);
    }
  }

  private static void iniciaServidorCallBack() throws IOException {
    // 1. Inicia servidor para ouvir callback
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    server.createContext("/callBack", new callBack());
    server.setExecutor(null);
    server.start();
  }

  static class callBack implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      String query = exchange.getRequestURI().getQuery();
      String code = null;
      if (query != null) {
        for (String param : query.split("&")) {
          if (param.startsWith("code=")) {
            code = param.substring(5);
            String response;
            if (code != null)
              response = "Autorizado com sucesso!";
            else
              response = "Falha ao obter autorização";
            exchange.getResponseHeaders().add("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
          }
        }
      }
    }
  }
}
