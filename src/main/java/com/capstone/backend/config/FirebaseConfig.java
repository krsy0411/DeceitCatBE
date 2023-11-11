package com.capstone.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.io.FileInputStream;

//파이어베이스 초기화
@Configuration
    public class FirebaseConfig {
        @PostConstruct
        public void init() {
            try {
                FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey.json");
                FirebaseOptions.Builder optionBuilder = FirebaseOptions.builder();
                FirebaseOptions options = optionBuilder
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                FirebaseApp.initializeApp(options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

