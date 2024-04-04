package com.jatin.repository;

import com.jatin.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    public Token findByUid(String uId);
}
