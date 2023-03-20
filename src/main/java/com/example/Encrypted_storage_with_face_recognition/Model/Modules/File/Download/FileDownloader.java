package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Download;

import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Converter.ByteToFileConverter;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Decryption.Decryptor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class FileDownloader {
    private final Decryptor decryptor;
    private final ByteToFileConverter byteToFileConverter;

    public FileDownloader(Decryptor decryptor, ByteToFileConverter byteToFileConverter) {
        this.decryptor = decryptor;
        this.byteToFileConverter = byteToFileConverter;
    }

    public File downloadFile(File file){
        Map<String, byte[]> fileData = decryptor.decrypt(file);

        //Дайджест никак не используется
        String fileName = new String(fileData.get("alias"));

        byte[] fileBytes = fileData.get("decryptedBytes");
        String filePath = "decrypted-files/" + fileName;

        return byteToFileConverter.convertAndSave(filePath, fileBytes);
    }
}
