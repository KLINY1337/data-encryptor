package com.example.Encrypted_storage_with_face_recognition.file.Service.EncryptionDecryptionHandling;

import com.example.Encrypted_storage_with_face_recognition.file.Service.FileHandling.LinkList.LinkListService;
import gov.sandia.cognition.util.DefaultTriple;
import gov.sandia.cognition.util.Triple;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.*;
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

    private final LinkListService linkListService;
    private final DigestService digestService;
    private final SecretKeyService secretKeyService;
    private KeyStore keyStore;
    private final String keyStoreFileName;
    private final String keyStoreType;
    private final String keyStorePassword;
    private final int aliasLength;
    private final String aliasAvailableCharacters;

    public KeyStoreService(@Value("${key.store.file.name}") String keyStoreFileName,
                           @Value("${key.store.type}") String keyStoreType,
                           @Value("${key.store.password}") String keyStorePassword,
                           @Value("${key.alias.length}") int aliasLength,
                           @Value("${key.alias.characters}") String aliasAvailableCharacters,
                           DigestService digestService,
                           LinkListService linkListService,
                           SecretKeyService secretKeyService) {
        this.linkListService = linkListService;

        this.keyStoreFileName = keyStoreFileName;
        this.keyStoreType = keyStoreType;

        this.keyStorePassword = keyStorePassword;

        this.aliasLength = aliasLength;
        this.aliasAvailableCharacters = aliasAvailableCharacters;

        this.digestService = digestService;
        this.secretKeyService = secretKeyService;
    }
    public KeyStore createKeyStore(){

        try {

            KeyStore keyStore = KeyStore.getInstance(keyStoreType);

            if (!linkListService.isLinkListExists()) {

                keyStore.load(null, keyStorePassword.toCharArray());

                FileOutputStream keyStoreOutputStream = new FileOutputStream(keyStoreFileName);

                keyStore.store(keyStoreOutputStream, keyStorePassword.toCharArray());

                keyStoreOutputStream.close();

            } else {

                FileInputStream keyStoreInputStream = new FileInputStream(keyStoreFileName);

                keyStore.load(keyStoreInputStream, keyStorePassword.toCharArray());

                keyStoreInputStream.close();
            }

            this.keyStore = keyStore;

            return keyStore;
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException e) {

            throw new RuntimeException(e);
        }
    }

    public KeyStore getKeyStore(){

        if (isKeyStoreExist()){

            return  keyStore;
        }
        else {

            return this.createKeyStore();
        }
    }
    public boolean isKeyStoreExist(){
        return keyStore != null;
    }

    @PreDestroy
    public void writeKeyStoreToFile(){

        try {

            keyStore.store(new FileOutputStream(keyStoreFileName), keyStorePassword.toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {

            throw new RuntimeException(e);
        }
    }

    public Triple<String, KeyStore.SecretKeyEntry, KeyStore.ProtectionParameter> getParametersForStoringKey(String filename,
                                                                                                            byte[] encryptedBytes,
                                                                                                            byte[] encryptedBytesDigest,
                                                                                                            SecretKey secretKey){
        //Добавить проверку чтобы этот альяс не содержался в списке файлов всех (этот альяс также будет именем шифрованного файла)
        String keyAlias;

        KeyStore.SecretKeyEntry secretKeyEntry;

        if (secretKey != null){

            secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);

            keyAlias = secretKeyService.getKeyAlias();
        }
        else {

            secretKeyEntry = null;

            keyAlias = filename;
        }

        KeyStore.ProtectionParameter entryPassword = secretKeyService.getEntryPassword(encryptedBytes, encryptedBytesDigest);

        return new DefaultTriple<>(keyAlias, secretKeyEntry, entryPassword);
    }
}
