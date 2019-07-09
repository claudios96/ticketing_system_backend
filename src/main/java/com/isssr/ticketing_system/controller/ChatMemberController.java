package com.isssr.ticketing_system.controller;

import com.isssr.ticketing_system.dao.ChatMemberDao;
import com.isssr.ticketing_system.entity.ChatMember;
import com.isssr.ticketing_system.enumeration.ChatType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ChatMemberController {

    private ChatMemberDao chatMemberDao;
    private ChatController chatController;

    @Autowired
    public ChatMemberController(
            ChatMemberDao chatMemberDao,
            @Lazy ChatController chatController) {
        this.chatMemberDao = chatMemberDao;
        this.chatController = chatController;
    }


    /**
     * Aggiunge un membro ad una chat.
     * type            - tipo della chat
     * subjectId       - id del prodotto/team/ticket in questione
     * userId          - utente da aggiungere
     */
    public ChatMember addChatMember(ChatType type, Long subjectId, Long userId) {
        ChatMember chatMember;
        Long chatId;

        // ottengo l'id del chat
        chatId = chatController.getChatId(type, subjectId);

        chatMember = new ChatMember(chatId, userId);

        chatMemberDao.save(chatMember);

        return chatMember;
    }

    public void removeMemberFromTeamChat(Long teamId, Long memberId){
        chatMemberDao.removeByChatIdAndUserId(chatController.getChatId(ChatType.TEAM, teamId), memberId);
    }

    /**
     *
     * @param chatId - id della chat della quale dovrebbe far parte
     * @param userId - id dello user da cercare
     * @return restituisce se l'utente ne fa parte o meno
     */
    public boolean partecipantExists(Long chatId, Long userId){
        ChatMember member = chatMemberDao.findByChatIdAndUserId(chatId, userId);

        return !(member == null);
    }
}
