package com.lrbell.llamabot.persistence.repository;

import com.lrbell.llamabot.persistence.model.ChatMessage;
import com.lrbell.llamabot.persistence.model.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {

    List<ChatMessage> findBySessionOrderByTimestampDesc(ChatSession session);
}
