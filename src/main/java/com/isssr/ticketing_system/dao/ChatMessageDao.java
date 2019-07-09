package com.isssr.ticketing_system.dao;

import com.isssr.ticketing_system.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageDao extends JpaRepository<ChatMessage, Long> {
}
