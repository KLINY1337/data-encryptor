package com.example.Encrypted_storage_with_face_recognition.Controller;


import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Download.FileDownloader;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.KeyStore.KeyStoreService;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.LinkList.Link;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.LinkList.LinkListService;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Upload.FileUploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping/*(value = "/api")*/
@Slf4j
public class WebController {

    @Autowired
    private KeyStoreService keyStoreService;

    @Autowired
    private LinkListService linkListService;

    @Autowired
    private FileUploader fileUploader;
    @Autowired
    private FileDownloader fileDownloader;

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

            return new ResponseEntity<>(fileUploader.uploadFile(file).getName(),
                    HttpStatus.OK);
        } catch (Exception exception) {
//           return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            throw new RuntimeException(exception);
        }
    }

//    TODO: fix [Request processing failed: java.lang.RuntimeException: java.lang.NullPointerException: Cannot invoke "java.security.KeyStore$SecretKeyEntry.getSecretKey()" because "entry" is null] with root cause
//
//java.lang.NullPointerException: Cannot invoke "java.security.KeyStore$SecretKeyEntry.getSecretKey()" because "entry" is null
//	at com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Decryption.Decryptor.decrypt(Decryptor.java:65) ~[classes/:na]
//	at com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Download.FileDownloader.downloadFile(FileDownloader.java:22) ~[classes/:na]
//	at com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Download.FileDownloader.downloadFile(FileDownloader.java:34) ~[classes/:na]
//	at com.example.Encrypted_storage_with_face_recognition.Controller.WebController.downloadFile(WebController.java:63) ~[classes/:na]

    @PostMapping(value = "/downloadFile")
    public ResponseEntity<Resource> downloadFile(@RequestParam("name") String name) {
        try {
            System.out.println("donloading");

            File file = fileDownloader.downloadFile(name);
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


}
