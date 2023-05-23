package com.example.Encrypted_storage_with_face_recognition.file.Service.FileHandling;

import com.example.Encrypted_storage_with_face_recognition.file.Service.EncryptionDecryptionHandling.DecryptionService;
import com.example.Encrypted_storage_with_face_recognition.file.Service.EncryptionDecryptionHandling.EncryptionService;
import com.example.Encrypted_storage_with_face_recognition.file.Service.FileHandling.LinkList.LinkListService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
public class FileTransferService {

    private final EncryptionService encryptionService;
    private final DecryptionService decryptionService;
    private final FileConverterService fileConverterService;

    private final LinkListService linkListService;

    public FileTransferService(EncryptionService encryptionService,
                               DecryptionService decryptionService, FileConverterService fileConverterService,
                               LinkListService linkListService) {

        this.encryptionService = encryptionService;
        this.decryptionService = decryptionService;
        this.fileConverterService = fileConverterService;
        this.linkListService = linkListService;
    }

    public File uploadFile(MultipartFile multipartFile){

        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename())); // create a new file with the original filename

        try {

            multipartFile.transferTo(file);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

        Map<String, byte[]> fileData = encryptionService.encrypt(file);

        //Дайджест никак не используется
        String fileName = new String(fileData.get("alias"));

        byte[] fileBytes = fileData.get("encryptedBytes");
        String filePath = "encrypted-files/" + fileName;

        File encryptedFile = fileConverterService.convertAndSave(filePath, fileBytes);

        linkListService.addLinkToList(file.getName(), encryptedFile.getName());

        return encryptedFile;
    }

    public File downloadFile(File file){

        Map<String, byte[]> fileData = decryptionService.decrypt(file);

        //Дайджест никак не используется
        String fileName = new String(fileData.get("alias"));

        byte[] fileBytes = fileData.get("decryptedBytes");
        String filePath = "decrypted-files/" + fileName;

        return fileConverterService.convertAndSave(filePath, fileBytes);
    }

    public File downloadFile(String encryptedFileName){

        return downloadFile(new File("encrypted-files/" + encryptedFileName));
    }
}
