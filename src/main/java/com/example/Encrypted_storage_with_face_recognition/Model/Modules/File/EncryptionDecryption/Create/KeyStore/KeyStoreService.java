package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.KeyStore;

import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.Digest.DigestService;
import gov.sandia.cognition.util.DefaultTriple;
import gov.sandia.cognition.util.Triple;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Slf4j
@Service
@PropertySource("application.properties")
public class KeyStoreService {
    private final DigestService digestService;
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
                           DigestService digestService) {

        this.keyStoreFileName = keyStoreFileName;
        this.keyStoreType = keyStoreType;
        this.keyStorePassword = keyStorePassword;

        this.aliasLength = aliasLength;
        this.aliasAvailableCharacters = aliasAvailableCharacters;

        this.digestService = digestService;
    }
    public KeyStore createKeyStore(){
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

            this.keyStore = keyStore;
            return keyStore;
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public Triple<String, KeyStore.SecretKeyEntry, KeyStore.ProtectionParameter> getParametersForStoringKey(byte[] encryptedBytes,
                                                                                                             byte[] encryptedBytesDigest,
                                                                                                             SecretKey secretKey){
        //Добавить проверку чтобы этот альяс не содержался в списке файлов всех (этот альяс также будет именем шифрованного файла)
        String keyAlias = getKeyAlias();

        KeyStore.SecretKeyEntry secretKeyEntry;
        if (secretKey != null){
            secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);
        }
        else {
            secretKeyEntry = null;
        }

        KeyStore.ProtectionParameter entryPassword = getEntryPassword(encryptedBytes, encryptedBytesDigest);

        return new DefaultTriple<>(keyAlias, secretKeyEntry, entryPassword);
    }

    public KeyStore getKeyStore(){
        if (isKeyStoreExist()){
            return  keyStore;
        }
        else{
            return this.createKeyStore();
        }
    }

    private String getKeyAlias(){

        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < aliasLength; i++) {
            int randomIndex = random.nextInt(aliasAvailableCharacters.length());
            stringBuilder.append(aliasAvailableCharacters.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }

    private String getKeyPassword(byte[] encryptedBytes, byte[] encryptedBytesDigest){
        byte[] shookBytesArray = getShookBytesArray(encryptedBytes, encryptedBytesDigest);

        for (int i = 0; i < shookBytesArray.length; i++) {
            if (shookBytesArray[i] < 127) {
                shookBytesArray[i] += 1;
            }
        }

        return new String(digestService.getDigest(shookBytesArray));
    }

    private static byte[] getShookBytesArray(byte[] encryptedBytes, byte[] encryptedBytesDigest) {
        List<Byte> shookBytesList = new ArrayList<>();

        for (byte element : encryptedBytes) {
            shookBytesList.add(element);
        }

        for (byte element : encryptedBytesDigest) {
            shookBytesList.add(element);
        }

        return ArrayUtils.toPrimitive(shookBytesList.toArray(new Byte[0]));
    }

    private KeyStore.ProtectionParameter getEntryPassword(byte[] encryptedBytes, byte[] encryptedBytesDigest) {
        String keyPassword = getKeyPassword(encryptedBytes, encryptedBytesDigest);
        return new KeyStore.PasswordProtection(keyPassword.toCharArray());
    }

    //TODO: properly check isKeyStoreExist
    public boolean isKeyStoreExist(){
        return keyStore != null;
    }


}
