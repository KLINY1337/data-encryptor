package com.example.Encrypted_storage_with_face_recognition.Model.Modules.Face.Config;

import com.github.sarxos.webcam.Webcam;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FaceConfig {

    @Bean
    public Webcam webcam(){
        return Webcam.getDefault();
    }

    @Bean
    public HaarCascadeDetector faceDetector(){
        return new HaarCascadeDetector();
    }
}
