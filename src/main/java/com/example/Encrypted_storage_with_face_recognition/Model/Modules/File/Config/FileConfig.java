package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Config;

import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Create.KeyStore.KeyStoreCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyStore;

@Configuration
public class FileConfig {

    @Bean
    public KeyStore keyStore() {
        return KeyStoreCreator.createKeyStore();
    }
}
