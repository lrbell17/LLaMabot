package com.lrbell.llamabot.repository;

import com.lrbell.llamabot.model.ChatSession;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, String> {

    @Modifying
    @Transactional
    @Query("UPDATE ChatSession c SET c.updatedAt = :updatedAt WHERE c.sessionId = :sessionId")
    int updateUpdatedAtById(@Param("sessionId") String sessionId, @Param("updatedAt") Instant updatedAt);
}
