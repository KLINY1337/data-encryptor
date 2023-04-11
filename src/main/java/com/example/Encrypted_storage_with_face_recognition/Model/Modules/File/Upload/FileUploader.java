package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Upload;

import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Converter.ByteToFileConverter;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.LinkList.LinkListService;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Encryption.Encryptor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;

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

    public File uploadFile(MultipartFile multipartFile){

        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename())); // create a new file with the original filename
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, byte[]> fileData = encryptor.encrypt(file);

        //Дайджест никак не используется
        String fileName = new String(fileData.get("alias"));

        byte[] fileBytes = fileData.get("encryptedBytes");
        String filePath = "encrypted-files/" + fileName;

        File encryptedFile = byteToFileConverter.convertAndSave(filePath, fileBytes);

        linkListService.addLinkToList(file.getName(), encryptedFile.getName());

        return encryptedFile;
    }

}
