package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.MetaData;

import com.google.common.primitives.Bytes;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileMetaDataService {
    public byte[] getFileMetaData(File file){
        String fileName = file.getName();

        return fileName.getBytes();
    }

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
