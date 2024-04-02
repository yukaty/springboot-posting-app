package com.example.postingapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.postingapp.entity.User;
import com.example.postingapp.entity.VerificationToken;
import com.example.postingapp.repository.VerificationTokenRepository;

@Service
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Transactional
    public void create(User user, String token) {
        VerificationToken verificationToken = new VerificationToken();

        verificationToken.setUser(user);
        verificationToken.setToken(token);

        verificationTokenRepository.save(verificationToken);
    }

    // Return the result of the search by token string
    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }
}

