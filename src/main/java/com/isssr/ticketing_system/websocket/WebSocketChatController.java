package com.isssr.ticketing_system.websocket;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.controller.ChatMessageController;
import com.isssr.ticketing_system.entity.ChatMessage;
import com.isssr.ticketing_system.enumeration.ChatMessageType;
import com.isssr.ticketing_system.response_entity.JsonViews;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketChatController {

    private static final Logger logger = LogManager.getLogger(WebSocketChatController.class);
    @Autowired
    ChatMessageController chatMessageController;

    @JsonView(JsonViews.MessageInfo.class)
    @MessageMapping("/c/{type}/{id_sub}")
    @SendTo("/t/{type}/{id_sub}")
    public ChatMessage sendMessage(String param) {

        ChatMessage message;
        Long chatId, userId;
        String[] params = null;

        try {
            params = toStringArray(new JSONArray(param));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        chatId = Long.valueOf(params[0]);
        userId = Long.valueOf(params[1]);

        // controllo sul tipo di messaggio
        ChatMessageType type = ChatMessageType.MESSAGE;

        switch (params[2]) {
            case "MESSAGE":
                type = ChatMessageType.MESSAGE;
                break;
            case "FILE":
                type = ChatMessageType.FILE;
                break;
            case "SNIPPET":
                type = ChatMessageType.SNIPPET;
                break;
        }

        message = chatMessageController.insertNewMessage(chatId, userId, params[3], type);

        return message;
    }

    public static String[] toStringArray(JSONArray array) {
        if(array==null)
            return null;

        String[] arr=new String[array.length()];
        for(int i=0; i<arr.length; i++) {
            arr[i]=array.optString(i);
        }
        return arr;
    }

}
