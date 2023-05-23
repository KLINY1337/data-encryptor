package com.example.Encrypted_storage_with_face_recognition.file.Util;

import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class ByteShaker {

    /**

     * Creates a byte array by combining the provided encrypted bytes and encrypted bytes digest.

     * @param encryptedBytes The encrypted bytes.

     * @param encryptedBytesDigest The digest of the encrypted bytes.

     * @return The byte array obtained by combining the encrypted bytes and encrypted bytes digest.
     */
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
