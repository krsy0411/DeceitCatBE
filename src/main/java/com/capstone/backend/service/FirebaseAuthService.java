package com.capstone.backend.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FirebaseAuthService {

    private final FirebaseAuth firebaseAuth;
    private final Logger logger = LoggerFactory.getLogger(FirebaseAuthService.class);

    public FirebaseAuthService(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public void createUserWithEmailAndPassword(String email, String password) {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);

            UserRecord userRecord = firebaseAuth.createUser(request);
            logger.info("Successfully created new user: {}", userRecord.getUid());
        } catch (FirebaseAuthException e) {
            logger.error("Error creating user: {}", e.getMessage(), e);
            // 예외 처리 로직 추가
        }
    }

    public void signInWithEmailAndPassword(String email, String password) {
        try {
            UserRecord userRecord = firebaseAuth.getUserByEmail(email);
            logger.info("Successfully fetched user data: {}", userRecord.getUid());
        } catch (FirebaseAuthException e) {
            logger.error("Error fetching user data: {}", e.getMessage(), e);
            // 예외 처리 로직 추가
        }
    }
}