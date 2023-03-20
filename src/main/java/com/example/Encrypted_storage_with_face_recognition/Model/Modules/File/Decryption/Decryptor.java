package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Decryption;

import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Create.KeyStore.KeyStoreService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.Map;

@Setter
@Getter
@Slf4j
@Service
public class Decryptor {

    private final KeyStoreService keyStoreService;

    public Decryptor(KeyStoreService keyStoreService){
        this.keyStoreService = keyStoreService;
    }

    /*public Map<String, byte[]> decrypt(){
        try {


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }*/
}
