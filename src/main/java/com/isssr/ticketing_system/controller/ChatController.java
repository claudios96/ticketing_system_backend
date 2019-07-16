package com.isssr.ticketing_system.controller;

import com.isssr.ticketing_system.dao.ChatDao;
import com.isssr.ticketing_system.entity.Chat;
import com.isssr.ticketing_system.enumeration.ChatType;
import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.slack.Slack;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ChatController {

    private ChatDao chatDao;
    private UserController userController;
    private ChatMemberController chatMemberController;

    @Autowired
    public ChatController(ChatDao chatDao, UserController userController, ChatMemberController chatMemberController) {
        this.chatDao = chatDao;
        this.userController = userController;
        this.chatMemberController = chatMemberController;
    }

    /**
     * @param type      - tipo della chat
     * @param subjectId - id dell'oggetto della chat(ticket/prodotto/team)
     * @return lista dei messaggi di una chat con all'interno
     */
    public Chat loadMessages(ChatType type, Long subjectId) {
        return chatDao.findByTypeAndSubjectId(type, subjectId);
    }


    /**
     * @param type      - tipo della chat
     * @param subjectId - id dell'oggetto della chat(ticket/prodotto/team)
     * @return oggetto chat creato
     */
    @Transactional
    public Chat createChat(ChatType type, Long subjectId, Boolean slackable, String slackAccount) {
        Chat chat;
        String slackChannelId = " ";

        if(type == ChatType.TICKET && slackable) {
            slackChannelId = new Slack().createConversation("ticket_" + subjectId, slackAccount);
            new Slack().sendMessage(slackChannelId, "Ticket created! - http://localhost:9000/#!/login", "System");
        }

        chat = new Chat(type, subjectId, slackChannelId);

        chatDao.save(chat);

        return chat;
    }

    /**
     *
     * @param type      - tipo della chat
     * @param subjectId - id dell'oggetto della chat(ticket/prodotto/team)
     * @return id della chat
     */
    public Long getChatId(ChatType type, Long subjectId){
        return chatDao.findByTypeAndSubjectId(type, subjectId).getId();
    }


    /**
     *
     * @param chatId - id della chat della quale vogliamo il corrispondente ID slack
     * @return ID del canale slack
     */
    public String getSlackBoardId(Long chatId){
        return chatDao.findById(chatId).get().getSlackBoardId();
    }

    /**
     *
     * @param ticketId id del ticket al quale si riferisce la chat
     * @return boolean che indica se esiste o meno
     */
    public boolean isChatExist(Long ticketId){
        Chat chat;
        boolean result = true;

        chat = chatDao.findByTypeAndSubjectId(ChatType.TICKET, ticketId);

        if(chat == null)
            result = false;

        return result;
    }

    /**
     * metodo che crea una cartella /uploadedFiles/chatId (se non presente) e salva
     * un file contenente la stringa in base64 data
     *
     * @param chatId
     * @param filename
     * @param data      stringa in base64 rappresentante il file
     */
    public void uploadFile(Long chatId, String filename, String data) {

        // MODIFICATO PER DOCKER
        //String path = "src/main/resources/uploadedFiles";
        String path = "risorseProgetto/uploadedFiles";

        // controllo l'esistenza della directory uploadedFiles
        if (Files.notExists(Paths.get(path))) {
            new File(path).mkdirs();
        }

        path = path.concat("/");
        path = path.concat(chatId.toString());

        // controllo l'esistenza della directory con nome chatId
        if (Files.notExists(Paths.get(path))) {
            new File(path).mkdirs();
        }

        // creo il file nella directory ../chatId
        path = path.concat("/");
        path = path.concat(filename);

        BufferedWriter output = null;
        try {
            File file = new File(path);
            output = new BufferedWriter(new FileWriter(file));
            output.write(data);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * @param chatId
     * @param filename
     * @return  ritorna una String contenente il file in base64
     */
    public String getFile(Long chatId, String filename) {

        // MODIFICATO PER DOCKER
        //String path = "src/main/resources/uploadedFiles";
        String path = "risorseProgetto/uploadedFiles";

        path = path.concat("/");
        path = path.concat(chatId.toString());
        path = path.concat("/");
        path = path.concat(filename);

        String data = "";

        if (Files.exists(Paths.get(path))) {
            try {
                data = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     *
     * @param type  - tipo dell chat
     * @param subjectId - oggetto della chat
     * @return risultato del controllo
     */
    public boolean userParticipationControl(ChatType type, Long subjectId){
        Long chatId, userId;
        String username;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails)
            username = ((UserDetails)principal).getUsername();
        else
            username = principal.toString();

        try {
            userId = userController.findUserByUsername(username).getId();
            chatId = getChatId(type, subjectId);
        }catch (EntityNotFoundException e){
            return false;
        }

        return chatMemberController.partecipantExists(chatId, userId);
    }
}
