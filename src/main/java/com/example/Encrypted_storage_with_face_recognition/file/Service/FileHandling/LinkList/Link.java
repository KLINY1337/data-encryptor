package com.example.Encrypted_storage_with_face_recognition.file.Service.FileHandling.LinkList;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public record Link(String decryptedFileName, String encryptedFileName) implements Serializable {

    private static final long serialVersionUID = 1L;
}