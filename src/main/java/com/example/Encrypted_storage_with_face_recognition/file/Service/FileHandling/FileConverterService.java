package com.example.Encrypted_storage_with_face_recognition.file.Service.FileHandling;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class FileConverterService {
     public File convertAndSave(String filePath, byte[] bytes){

         try {

             File file = new File(filePath);

             FileOutputStream fileOutputStream = new FileOutputStream(file);

             fileOutputStream.write(bytes);

             fileOutputStream.close();

             return file;
         } catch (IOException e) {

             throw new RuntimeException(e);
         }
     }
}
