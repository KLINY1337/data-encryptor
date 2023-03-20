package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Converter;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class ByteToFileConverter {
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
