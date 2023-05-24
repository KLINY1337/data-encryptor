package com.example.Encrypted_storage_with_face_recognition.file.Controller;


import com.example.Encrypted_storage_with_face_recognition.file.Service.FileHandling.FileTransferService;
import com.example.Encrypted_storage_with_face_recognition.file.Service.EncryptionDecryptionHandling.KeyStoreService;
import com.example.Encrypted_storage_with_face_recognition.file.Service.FileHandling.LinkList.Link;
import com.example.Encrypted_storage_with_face_recognition.file.Service.FileHandling.LinkList.LinkListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping/*(value = "/api")*/
@Slf4j
public class WebController {

    private final KeyStoreService keyStoreService;

    private final LinkListService linkListService;

    private final FileTransferService fileTransferService;

    public WebController(KeyStoreService keyStoreService, LinkListService linkListService, FileTransferService fileTransferService) {
        this.keyStoreService = keyStoreService;
        this.linkListService = linkListService;
        this.fileTransferService = fileTransferService;
    }

    @RequestMapping(value = "/isKeyStoreExist")
    public ResponseEntity<?> page() {
        try {
            return ResponseEntity.ok(new HashMap<String, Boolean>() {{
                put("isKeyStoreExist", keyStoreService.isKeyStoreExist());
            }});
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }


    @PostMapping(value = "/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("uploading");

            return new ResponseEntity<>(fileTransferService.uploadFile(file).getName(),
                    HttpStatus.OK);
        } catch (Exception exception) {
//           return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            throw new RuntimeException(exception);
        }
    }

//    TODO: fix [Request processing failed: java.lang.RuntimeException: java.lang.NullPointerException: Cannot invoke "java.security.KeyStore$SecretKeyEntry.getSecretKey()" because "entry" is null] with root cause
//
//java.lang.NullPointerException: Cannot invoke "java.security.KeyStore$SecretKeyEntry.getSecretKey()" because "entry" is null
//	at com.example.Encrypted_storage_with_face_recognition.file.Decryptor.decrypt(Decryptor.java:65) ~[classes/:na]
//	at com.example.Encrypted_storage_with_face_recognition.file.FileDownloader.downloadFile(FileDownloader.java:22) ~[classes/:na]
//	at com.example.Encrypted_storage_with_face_recognition.file.FileDownloader.downloadFile(FileDownloader.java:34) ~[classes/:na]
//	at com.example.Encrypted_storage_with_face_recognition.file.Controller.WebController.downloadFile(WebController.java:63) ~[classes/:na]

    @PostMapping(value = "/downloadFile")
    public ResponseEntity<Resource> downloadFile(@RequestParam("name") String name) {
        try {
            System.out.println("donloading");

            File file = fileTransferService.downloadFile(name);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment;filename=" + file.getName())
                    .body(resource);
        } catch (Exception exception) {
//           return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            throw new RuntimeException(exception);

        }
    }

    @GetMapping(value = "/getLinkList")
    public List<Link> getLinkList() {
        return linkListService.getLinkList();
    }

    @GetMapping(value = "/getLinkListLength")
    public int getLinkListLength() {
        return linkListService.getLinkList().size();
    }


    @GetMapping(value = "/getLinkList/mock")
    public ResponseEntity<List<Map<String, String>>> getFiles() {
        List<Map<String, String>> fileList = new ArrayList<>();

        // Sample file data
        fileList.add(createFileEntry("document.pdf", "encrypted_document.pdf"));
        fileList.add(createFileEntry("image.jpg", "encrypted_image.jpg"));
        fileList.add(createFileEntry("video.mp4", "encrypted_video.mp4"));

        return ResponseEntity.status(HttpStatus.OK).body(fileList);
    }

    private Map<String, String> createFileEntry(String fileName, String encryptedFileName) {
        Map<String, String> fileEntry = new HashMap<>();
        fileEntry.put("decryptedFileName", fileName);
        fileEntry.put("encryptedFileName", encryptedFileName);
        return fileEntry;
    }


}
