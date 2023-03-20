package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Upload;

import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Converter.ByteToFileConverter;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Encryption.Encryptor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class FileUploader {

    private final Encryptor encryptor;
    private final ByteToFileConverter byteToFileConverter;

    public FileUploader(Encryptor encryptor, ByteToFileConverter byteToFileConverter) {
        this.encryptor = encryptor;
        this.byteToFileConverter = byteToFileConverter;
    }

    public File uploadFile(File file){
        Map<String, byte[]> fileData = encryptor.encrypt(file);

        //Дайджест никак не используется
        String fileName = new String(fileData.get("alias"));

        byte[] fileBytes = fileData.get("encryptedBytes");
        String filePath = "encrypted-files/" + fileName;

        return byteToFileConverter.convertAndSave(filePath, fileBytes);
    }
}
