package com.lrbell.llamabot.persistence.repository;

import com.lrbell.llamabot.persistence.model.ChatMessage;
import com.lrbell.llamabot.persistence.model.ChatSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {

    Page<ChatMessage> findBySessionOrderByTimestampDesc(ChatSession session, Pageable pageable);
}
