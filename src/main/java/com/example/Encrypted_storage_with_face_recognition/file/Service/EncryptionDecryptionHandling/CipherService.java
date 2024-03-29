package com.example.Encrypted_storage_with_face_recognition.file.Service.EncryptionDecryptionHandling;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Setter
@Getter
@Service
@PropertySource("application.properties")
public class CipherService {
    private final String encryptionMethod;

    public CipherService(@Value("${cipher.encryption.method}") String encryptionMethod){

        this.encryptionMethod = encryptionMethod;
    }

    /**
    * Returns a {@link Cipher} object for encryption or decryption based on the specified mode and secret key.

    * @param MODE the encryption or decryption mode (use {@link Cipher#ENCRYPT_MODE} or {@link Cipher#DECRYPT_MODE})

    * @param secretKey the secret key used for encryption or decryption

    * @return a {@link Cipher} object configured for the specified mode and secret key

    * @throws RuntimeException if an error occurs during the initialization of the cipher
     */
    public Cipher getCipher(int MODE,SecretKey secretKey) {

        try {

            if (MODE == Cipher.ENCRYPT_MODE){

                Cipher cipher = Cipher.getInstance(encryptionMethod);

                byte[] iv = new byte[cipher.getBlockSize()];
                IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

                cipher.init(MODE, secretKey, ivParameterSpec);

                return cipher;
            }
            else if (MODE == Cipher.DECRYPT_MODE) {

                Cipher cipher = Cipher.getInstance(encryptionMethod);

                byte[] ivByte = new byte[cipher.getBlockSize()];
                IvParameterSpec ivParameterSpec = new IvParameterSpec(ivByte);

                cipher.init(MODE, secretKey, ivParameterSpec);

                return cipher;
            }
            return null;

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }
}
