package com.example.Encrypted_storage_with_face_recognition.file.Service.FileHandling;

import com.google.common.primitives.Bytes;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileMetaDataService {

    /**

     * Retrieves the metadata of the given file.
     * @param file The file to extract metadata from.
     * @return The byte array representing the file metadata.
     */
    public byte[] getFileMetaData(File file){

        return file.getName().getBytes();
    }

    /**

    * Separates the file data from the given decrypted bytes and returns a map containing the separated data.

    * @param decryptedBytes The decrypted bytes of the file.

    * @return A map containing the separated file data, with "fileBytes" as the key for the file bytes and "fileMetaData" as the key for the file metadata.
     */
    public Map<String, byte[]> getSeparatedFileData(byte[] decryptedBytes) {

        byte fileMetaDataLength = decryptedBytes[decryptedBytes.length - 1];

        List<Byte> fileMetaData = new ArrayList<>();

        for (int i = decryptedBytes.length - fileMetaDataLength - 1; i < decryptedBytes.length - 1; i++) {

            fileMetaData.add(decryptedBytes[i]);
        }

        List<Byte> fileBytes = new ArrayList<>();
        for (int i = 0; i < decryptedBytes.length - fileMetaDataLength - 1; i++) {

            fileBytes.add(decryptedBytes[i]);
        }

        Map<String, byte[]> separatedFileData = new HashMap<>();

        separatedFileData.put("fileBytes", Bytes.toArray(fileBytes));
        separatedFileData.put("fileMetaData", Bytes.toArray(fileMetaData));

        return separatedFileData;
    }
}
