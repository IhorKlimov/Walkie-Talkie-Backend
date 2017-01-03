import org.eclipse.jetty.websocket.api.Session;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.*;



public class Main {
  static Map<Session, String> userUsernameMap = new ConcurrentHashMap<>();
  static int nextUserNumber = 1; //Assign to username for next connecting user

  public static void main(String[] args) {

    port(Integer.valueOf(System.getenv("PORT")));
    staticFileLocation("/public");

    webSocket("/chat", ChatWebSocketHandler.class);
    init();

    get("/hello", (req, res) -> Integer.valueOf(System.getenv("PORT")));

    get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World!");

            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerEngine());
  }

  public static void broadcastMessage(Session sender, String message) {
    userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
      if (session != sender) {
        try {
          session.getRemote().sendString(message);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
}
