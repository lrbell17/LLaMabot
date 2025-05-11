package com.lrbell.llamabot.persistence.repository;

import com.lrbell.llamabot.persistence.model.ChatSession;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, String> {

    Page<ChatSession> findByUserIdOrderByUpdatedAtDesc(String userId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE ChatSession c SET c.updatedAt = :updatedAt WHERE c.sessionId = :sessionId")
    int updateUpdatedAtById(@Param("sessionId") String sessionId, @Param("updatedAt") Instant updatedAt);
}
