package com.example.Encrypted_storage_with_face_recognition.file.Service.FileHandling.LinkList;

import java.io.Serializable;

/**

 * A record representing a link between a decrypted file name and an encrypted file name.
 * The Link record implements the Serializable interface.
 * @param decryptedFileName The name of the decrypted file.
 * @param encryptedFileName The name of the encrypted file.
 */
public record Link(String decryptedFileName, String encryptedFileName) implements Serializable {

    private static final long serialVersionUID = 1L;
}