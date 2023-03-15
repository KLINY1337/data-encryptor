package com.example.Encrypted_storage_with_face_recognition;

import com.example.Encrypted_storage_with_face_recognition.Model.Modules.Face.Recognition.FaceRecognizer;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Encryption.Encryptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@SpringBootApplication
public class EncryptedStorageWithFaceRecognitionApplication {

	public static void main(String[] args) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
		SpringApplication.run(EncryptedStorageWithFaceRecognitionApplication.class, args);
		FaceRecognizer faceRecognizer = new FaceRecognizer();
		System.out.println(faceRecognizer.detectFace());

		System.out.println(Arrays.toString(Encryptor.encrypt(new File("LICENSE.txt"))));
	}

}
