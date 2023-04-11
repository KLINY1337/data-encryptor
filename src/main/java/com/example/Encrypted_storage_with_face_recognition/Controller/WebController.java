package com.example.Encrypted_storage_with_face_recognition.Controller;


import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.KeyStore.KeyStoreService;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Upload.FileUploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping/*(value = "/api")*/
@Slf4j
public class WebController {

    @Autowired
    private KeyStoreService keyStoreService;

    @Autowired
    private FileUploader fileUploader;

    @RequestMapping(value = "/isKeyStoreExist")
    public ResponseEntity<?> page() {
        try {
            return ResponseEntity.ok(new HashMap<String,Boolean>(){{put("isKeyStoreExist",keyStoreService.isKeyStoreExist());}});
        }
        catch (Exception exception){
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }


    @PostMapping(value = "/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
        try {
            System.out.println("uploading");

           return new ResponseEntity<>(fileUploader.uploadFile(file).getName(),
                    HttpStatus.OK);
        }catch (Exception exception){
//           return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            throw new RuntimeException(exception);
        }
    }

}
