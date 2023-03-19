package com.example.Encrypted_storage_with_face_recognition.Controller;


import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Create.KeyStore.KeyStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/")
@Slf4j
public class WebController {

    @Autowired
    private KeyStoreService keyStoreService;

    @RequestMapping(value = "/isKeyStoreExist")
    public ResponseEntity<?> page() {
        try {
            return ResponseEntity.ok(new HashMap<String,Boolean>(){{put("isKeyStoreExist",keyStoreService.isKeyStoreExist());}});
        }
        catch (Exception exception){
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }


}
