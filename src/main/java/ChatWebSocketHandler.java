import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@WebSocket
public class ChatWebSocketHandler {

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = "User" + Main.nextUserNumber++;
        Main.userUsernameMap.put(user, username);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        Main.userUsernameMap.remove(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        System.out.println(message);
        Main.broadcastMessage(user, message);
    }

}
