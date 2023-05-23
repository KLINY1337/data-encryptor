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

    /**

     * Creates a new KeyStore instance and initializes it.

     * @return the created KeyStore object

     * @throws RuntimeException if an error occurs during the creation or initialization of the KeyStore

     * @throws CertificateException if an error occurs while processing a certificate

     * @throws KeyStoreException if an error occurs with the KeyStore

     * @throws IOException if an I/O error occurs

     * @throws NoSuchAlgorithmException if the specified algorithm for the KeyStore is not available
     */
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

    /**

     * Returns the KeyStore instance. If the KeyStore exists, it is returned directly. Otherwise, a new KeyStore is created and returned.
     * @return the KeyStore object
     * @throws RuntimeException if an error occurs during the creation or initialization of the KeyStore
     * @see #createKeyStore()
     */
    public KeyStore getKeyStore(){

        if (isKeyStoreExist()){

            return  keyStore;
        }
        else {

            return this.createKeyStore();
        }
    }

    /**

     * Checks if the KeyStore instance exists.
     * @return true if the KeyStore exists, false otherwise
     */
    public boolean isKeyStoreExist(){
        return keyStore != null;
    }

    /**

     * Writes the KeyStore to a file before destroying the object.
     * @throws RuntimeException if an error occurs while writing the KeyStore to the file
     * @throws KeyStoreException if an error occurs with the KeyStore
     * @throws IOException if an I/O error occurs
     * @throws NoSuchAlgorithmException if the specified algorithm for the KeyStore is not available
     * @throws CertificateException if an error occurs while processing a certificate
     */
    @PreDestroy
    public void writeKeyStoreToFile(){

        try {

            keyStore.store(new FileOutputStream(keyStoreFileName), keyStorePassword.toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {

            throw new RuntimeException(e);
        }
    }

    /**

     * Generates the necessary parameters for storing a key in the KeyStore.

     * @param filename the name of the file (alias) associated with the key, or null if a secret key is provided

     * @param encryptedBytes the encrypted bytes to be associated with the key

     * @param encryptedBytesDigest the digest (hash) of the encrypted bytes

     * @param secretKey the secret key, or null if a filename (alias) is provided

     * @return a Triple object containing the key alias, secret key entry, and protection parameter for storing the key
     */
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
