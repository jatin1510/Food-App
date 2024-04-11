package com.jatin.service;

import com.jatin.model.Token;
import com.jatin.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenService {
    private static final long EXPIRATION_TIME = 600000; // 10 minutes in milliseconds

    @Autowired
    private TokenRepository tokenRepository;

    public String generateToken(String email) {
        Token token = new Token();
        token.setUid(UUID.randomUUID().toString());
        token.setIssuedAt(System.currentTimeMillis());
        token.setEmail(email);
        Token generatedToken = tokenRepository.save(token);
        return generatedToken.getUid();
    }

    public boolean verifyToken(String uId) {
        Token token = tokenRepository.findByUid(uId);
        if (token == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        long issuedAt = token.getIssuedAt();

        return currentTime < issuedAt + EXPIRATION_TIME;
    }

    public void removeToken(String uId) {
        Token token = tokenRepository.findByUid(uId);
        if (token != null) {
            tokenRepository.delete(token);
        }
    }

    public String getEmail(String uId) throws Exception {
        Token token = tokenRepository.findByUid(uId);
        if (token == null) {
            throw new Exception("Not Found");
        }
        return token.getEmail();
    }
}
