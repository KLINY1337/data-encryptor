package com.example.Encrypted_storage_with_face_recognition.file.Util;

import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class ByteShaker {
    public static byte[] getShookBytesArray(byte[] encryptedBytes, byte[] encryptedBytesDigest) {

        List<Byte> shookBytesList = new ArrayList<>();

        for (byte element : encryptedBytes) {

            shookBytesList.add(element);
        }

        for (byte element : encryptedBytesDigest) {

            shookBytesList.add(element);
        }

        return ArrayUtils.toPrimitive(shookBytesList.toArray(new Byte[0]));
    }

}
