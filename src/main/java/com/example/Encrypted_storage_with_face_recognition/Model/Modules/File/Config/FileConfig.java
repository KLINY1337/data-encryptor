package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyStore;
import java.security.KeyStoreException;

@Configuration
public class FileConfig {

    @Bean
    public KeyStore keyStore() throws KeyStoreException {
        return KeyStore.getInstance("jks");
    }
}
