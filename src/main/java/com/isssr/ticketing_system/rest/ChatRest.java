package com.isssr.ticketing_system.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.controller.ChatController;
import com.isssr.ticketing_system.entity.Chat;
import com.isssr.ticketing_system.enumeration.ChatType;
import com.isssr.ticketing_system.response_entity.JsonViews;
import com.isssr.ticketing_system.response_entity.ResponseEntityBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("chat")
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEAM_COORDINATOR', 'ROLE_TEAM_LEADER', 'ROLE_TEAM_MEMBER')")
public class ChatRest {

    private ChatController chatController;

    @Autowired
    public ChatRest(ChatController chatController) {
        this.chatController = chatController;
    }


    @JsonView(JsonViews.ChatMessagesLoadInfo.class)
    @RequestMapping(path = "msgs", method = RequestMethod.GET)
    public ResponseEntity getMessages(@RequestParam("type") ChatType type, @RequestParam("subject_id") Long subjectId) {
        Chat chat;

        if(chatController.userParticipationControl(type, subjectId)) {
            chat = chatController.loadMessages(type, subjectId);
            return new ResponseEntityBuilder<>(chat).setStatus(HttpStatus.OK).build();
        }else{
            return new ResponseEntityBuilder<>().setStatus(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(path = "exists", method = RequestMethod.GET)
    public ResponseEntity chatIsCreated(@RequestParam("ticket_id") Long ticketId){
        Boolean result;

        result = chatController.isChatExist(ticketId);

        return new ResponseEntityBuilder<>(result).setStatus(HttpStatus.OK).build();
    }


    @RequestMapping(path = "uploadFile", method = RequestMethod.POST)
    public ResponseEntity uploadFileFromClient(@RequestParam("chat_id") Long chatId, @RequestParam("filename") String filename, @RequestBody String data){

        chatController.uploadFile(chatId, filename, data);

        return new ResponseEntityBuilder<>("OK").setStatus(HttpStatus.OK).build();
    }


    @RequestMapping(path = "getFile", method = RequestMethod.GET)
    public ResponseEntity getFileFromClient(@RequestParam("chat_id") Long chatId, @RequestParam("filename") String filename){

        String data = chatController.getFile(chatId, filename);

        if (data.compareTo("") == 0) {
            return new ResponseEntityBuilder<>(data).setStatus(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return new ResponseEntityBuilder<>(data).setStatus(HttpStatus.OK).build();
    }

}
