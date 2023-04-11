package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Upload;

import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Converter.ByteToFileConverter;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.LinkList.LinkListService;
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

    private final LinkListService linkListService;

    public FileUploader(Encryptor encryptor,
                        ByteToFileConverter byteToFileConverter,
                        LinkListService linkListService) {
        this.encryptor = encryptor;
        this.byteToFileConverter = byteToFileConverter;
        this.linkListService = linkListService;
    }

    public File uploadFile(File file){
        Map<String, byte[]> fileData = encryptor.encrypt(file);

        //Дайджест никак не используется
        String fileName = new String(fileData.get("alias"));

        byte[] fileBytes = fileData.get("encryptedBytes");
        String filePath = "encrypted-files/" + fileName;

        File encryptedFile = byteToFileConverter.convertAndSave(filePath, fileBytes);

        linkListService.addLinkToList(file.getName(), encryptedFile.getName());

        return encryptedFile;
    }

    public File uploadFile(MultipartFile file){

        File converted = new File(file.getOriginalFilename());
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
