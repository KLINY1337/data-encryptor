package com.example.Encrypted_storage_with_face_recognition;

import com.example.Encrypted_storage_with_face_recognition.Model.Modules.Face.Recognition.FaceRecognizer;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Encryption.Encryptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class EncryptedStorageWithFaceRecognitionApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(EncryptedStorageWithFaceRecognitionApplication.class, args);
		FaceRecognizer faceRecognizer = new FaceRecognizer();
		System.out.println(faceRecognizer.detectFace());

		System.out.println(Encryptor.encrypt(new File("LICENSE.txt")));
	}

}
