package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Create.KeyStore;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Setter
@Getter
@Slf4j
@Service
@PropertySource("application.properties")
public class KeyStoreService {
    //@Value("${key.store.file.name}")
    private static String keyStoreFileName;

    //@Value("${key.store.type}")
    private static String keyStoreType;

    //@Value("${key.store.password}")
    private static String keyStorePassword;

    public KeyStoreService(@Value("${key.store.file.name}") String keyStoreFileName,
                           @Value("${key.store.type}") String keyStoreType,
                           @Value("${key.store.password}") String keyStorePassword) {

        KeyStoreService.keyStoreFileName = keyStoreFileName;
        KeyStoreService.keyStoreType = keyStoreType;
        KeyStoreService.keyStorePassword = keyStorePassword;
    }
    public static KeyStore createKeyStore(){
        try {
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);

            if (!Files.exists(Path.of(keyStoreFileName))) {

                keyStore.load(null, keyStorePassword.toCharArray());

                FileOutputStream keyStoreOutputStream = new FileOutputStream(keyStoreFileName);

                keyStore.store(keyStoreOutputStream, keyStorePassword.toCharArray());

                keyStoreOutputStream.close();

            } else {
                FileInputStream keyStoreInputStream = new FileInputStream(keyStoreFileName);
                keyStore.load(keyStoreInputStream, keyStorePassword.toCharArray());
                keyStoreInputStream.close();
            }

            return keyStore;
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isKeyStoreExist(){
        return Files.exists(Path.of(keyStoreFileName));
    }
}
