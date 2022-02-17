package com.nikolay.task4.repo;

import com.nikolay.task4.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "SELECT * FROM MESSAGES WHERE RECEIVER = ?1 ORDER BY TIME DESC", nativeQuery = true)
    Iterable<Message> findAllByUsername(String username);
}
