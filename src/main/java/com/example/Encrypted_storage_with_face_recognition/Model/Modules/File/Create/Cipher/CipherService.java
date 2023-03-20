package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Create.Cipher;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
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
    public Cipher getCipher(int MODE,SecretKey secretKey) {
        try {
                Cipher cipher = Cipher.getInstance(encryptionMethod);
                cipher.init(MODE, secretKey);

                return cipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
