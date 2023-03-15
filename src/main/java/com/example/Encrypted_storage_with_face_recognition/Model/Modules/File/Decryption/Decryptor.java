package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Decryption;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyStore;

@Setter
@Getter
@Slf4j
@Service
public class Decryptor {

    @Autowired
    private KeyStore keyStore;


}
