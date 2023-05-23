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

    /**

     * Uploads a file by saving it locally, encrypting it, and storing the encrypted file. It also adds a link to the link list for future reference.

     * @param multipartFile The file to be uploaded, wrapped in a {@link MultipartFile} object.

     * @return The encrypted file that has been uploaded.

     * @throws RuntimeException If an error occurs during the file upload or encryption process.
     */
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


    /**

     * Downloads a file by decrypting it and storing the decrypted file locally.

     * @param file The file to be downloaded.

     * @return The decrypted file that has been downloaded.

     * @throws RuntimeException If an error occurs during the decryption process or file conversion process.
     */
    public File downloadFile(File file){

        Map<String, byte[]> fileData = decryptionService.decrypt(file);

        //Дайджест никак не используется
        String fileName = new String(fileData.get("alias"));

        byte[] fileBytes = fileData.get("decryptedBytes");
        String filePath = "decrypted-files/" + fileName;

        return fileConverterService.convertAndSave(filePath, fileBytes);
    }

    /**

     * Downloads a file with the given encrypted file name by decrypting it and storing the decrypted file locally.
     * @param encryptedFileName The name of the encrypted file to be downloaded.
     * @return The decrypted file that has been downloaded.
     * @throws RuntimeException If an error occurs during the decryption process or file conversion process.
     */
    public File downloadFile(String encryptedFileName){

        return downloadFile(new File("encrypted-files/" + encryptedFileName));
    }
}
