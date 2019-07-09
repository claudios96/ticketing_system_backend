package com.isssr.ticketing_system.dao;

import com.isssr.ticketing_system.entity.Chat;
import com.isssr.ticketing_system.enumeration.ChatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatDao extends JpaRepository<Chat, Long> {
    Chat findByTypeAndSubjectId(ChatType type, Long subjectId);
}
