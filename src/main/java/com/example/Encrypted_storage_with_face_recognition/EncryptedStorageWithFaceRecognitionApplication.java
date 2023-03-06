package com.example.Encrypted_storage_with_face_recognition;

import com.github.sarxos.webcam.Webcam;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class EncryptedStorageWithFaceRecognitionApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(EncryptedStorageWithFaceRecognitionApplication.class, args);
		Webcam webcam = Webcam.getDefault();
		webcam.open();
		ImageIO.write(webcam.getImage(),"PNG", new File("hello-world.png"));
		webcam.close();
	}

}
