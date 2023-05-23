package com.example.Encrypted_storage_with_face_recognition.file.Service.EncryptionDecryptionHandling;

import com.example.Encrypted_storage_with_face_recognition.file.Util.ByteShaker;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Setter
@Getter
@Service
@PropertySource("application.properties")
public class SecretKeyService {
    private final String generationMethod;
    private final int keyBitSize;
    private final int aliasLength;
    private final String aliasAvailableCharacters;
    private final DigestService digestService;

    public SecretKeyService(@Value("${secret.key.generation.method}") String generationMethod,
                            @Value("${secret.key.bit.size}") int keyBitSize,
                            @Value("${key.alias.length}") int aliasLength,
                            @Value("${key.alias.characters}") String aliasAvailableCharacters,
                            DigestService digestService) {

        this.generationMethod = generationMethod;
        this.keyBitSize = keyBitSize;
        this.aliasLength = aliasLength;
        this.aliasAvailableCharacters = aliasAvailableCharacters;
        this.digestService = digestService;
    }


    /**

     * Generates and returns a SecretKey using the specified generation method.
     * @return the generated SecretKey
     * @throws RuntimeException if an error occurs during the generation of the SecretKey
     * @throws NoSuchAlgorithmException if the specified generation method is not available
     */
    public SecretKey getSecretKey() {

        try {

            KeyGenerator keyGenerator = KeyGenerator.getInstance(generationMethod);
            SecureRandom secureRandom = new SecureRandom();

            keyGenerator.init(keyBitSize, secureRandom);

            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(e);
        }
    }

    /**

     * Generates and returns a random key alias.

     * @return the generated key alias
     */
    public String getKeyAlias(){

        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < aliasLength; i++) {

            int randomIndex = random.nextInt(aliasAvailableCharacters.length());

            stringBuilder.append(aliasAvailableCharacters.charAt(randomIndex));
        }

        return stringBuilder.toString();
    }

    /**

     * Generates and returns a key password based on the provided encrypted bytes and encrypted bytes digest.

     * @param encryptedBytes the encrypted bytes

     * @param encryptedBytesDigest the digest (hash) of the encrypted bytes

     * @return the generated key password
     */
    public String getKeyPassword(byte[] encryptedBytes, byte[] encryptedBytesDigest){

        byte[] shookBytesArray = ByteShaker.getShookBytesArray(encryptedBytes, encryptedBytesDigest);

        for (int i = 0; i < shookBytesArray.length; i++) {

            if (shookBytesArray[i] < 127) {

                shookBytesArray[i] += 1;
            }
        }

        return Base64.getEncoder().encodeToString(digestService.getDigest(shookBytesArray));
    }

    /**

     * Generates and returns a ProtectionParameter for protecting a KeyStore entry, based on the provided
     * encrypted bytes and encrypted bytes digest.
     * @param encryptedBytes the encrypted bytes
     * @param encryptedBytesDigest the digest (hash) of the encrypted bytes
     * @return the generated ProtectionParameter
     */

    public KeyStore.ProtectionParameter getEntryPassword(byte[] encryptedBytes, byte[] encryptedBytesDigest) {

        String keyPassword = getKeyPassword(encryptedBytes, encryptedBytesDigest);

        return new KeyStore.PasswordProtection(keyPassword.toCharArray());
    }
}
