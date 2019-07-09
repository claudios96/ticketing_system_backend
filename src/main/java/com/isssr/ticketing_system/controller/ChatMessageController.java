package com.isssr.ticketing_system.controller;

import com.isssr.ticketing_system.dao.ChatMessageDao;
import com.isssr.ticketing_system.entity.ChatMessage;
import com.isssr.ticketing_system.entity.User;
import com.isssr.ticketing_system.enumeration.ChatMessageType;
import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.slack.Slack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ChatMessageController {
    private ChatMessageDao chatMessageDao;
    private UserController userController;
    private ChatController chatController;

    @Autowired
    public ChatMessageController(ChatMessageDao chatMessageDao,
                                 UserController userController,
                                 ChatController chatController)
    {
        this.chatMessageDao = chatMessageDao;
        this.userController = userController;
        this.chatController = chatController;
    }


    /**
     * @param textBody - testo del messaggio comprensivo dei vari tag
     * @param chatId   - id della chat
     * @param userId   - id globale dell'utente dalla tabella users
     * @return messaggio appena inserito
     */
    public ChatMessage insertNewMessage(Long chatId, Long userId, String textBody, ChatMessageType type) {
        ChatMessage message;
        User user = null;
        String username;
        String completeName = "";
        String slackBoardiD;

        if(userId != -1) {  // non è utente automatico system
            try {
                user = userController.findById(userId);

            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }

            username = user.getUsername();
            completeName = user.getFirstName() + " " + user.getLastName();

        }else
            username = "System";

        slackBoardiD = chatController.getSlackBoardId(chatId);

        // aggiunto messageType
        message = new ChatMessage(chatId, textBody, Instant.now(), username, completeName, type);
        new Slack().sendMessage(slackBoardiD, textBody, completeName);

        chatMessageDao.save(message);

        return message;
    }

}
