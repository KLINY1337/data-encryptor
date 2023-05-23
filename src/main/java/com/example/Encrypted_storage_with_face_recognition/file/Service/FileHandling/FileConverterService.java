package com.example.Encrypted_storage_with_face_recognition.file.Service.FileHandling;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class FileConverterService {

    /**

     * Converts the given byte array into a file and saves it at the specified file path.
     * @param filePath The path where the file should be saved.
     * @param bytes The byte array to be converted and saved as a file.
     * @return The created file.
     * @throws RuntimeException if an error occurs while converting and saving the file
     * @throws IOException if an I/O error occurs
     */
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
