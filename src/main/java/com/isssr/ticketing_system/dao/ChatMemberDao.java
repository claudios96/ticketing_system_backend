package com.isssr.ticketing_system.dao;

import com.isssr.ticketing_system.entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMemberDao extends JpaRepository<ChatMember, Long> {
    ChatMember findByChatIdAndUserId(Long chatId, Long userId);
    void removeByChatIdAndUserId(Long chatId, Long userId);
}
