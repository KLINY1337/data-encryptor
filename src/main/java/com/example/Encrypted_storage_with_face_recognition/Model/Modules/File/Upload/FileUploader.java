package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Upload;

import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Converter.ByteToFileConverter;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Encryption.Encryptor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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

    public File uploadFile(MultipartFile file){
        File converted = new File("uploading.tmp");
        try{
            try (OutputStream os = new FileOutputStream(converted)) {
                os.write(file.getBytes());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return uploadFile(converted);
    }
}
