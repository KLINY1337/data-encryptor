package com.example.Encrypted_storage_with_face_recognition.Model.Modules.Face_recognition;

import com.example.Encrypted_storage_with_face_recognition.Model.Modules.Face_getting.FaceGetter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;

import java.awt.image.BufferedImage;
import java.util.List;

@Setter
@Getter
@Slf4j
public class FaceRecognizer {
    private HaarCascadeDetector faceDetector = new HaarCascadeDetector();

    public DetectedFace detectFace(){
        BufferedImage faceImage = FaceGetter.getFaceImage();

        try {
            assert faceImage != null;
            List<DetectedFace> detectedFaces = faceDetector.detectFaces(ImageUtilities.createFImage(faceImage));

            if (detectedFaces.size() != 1){
                log.error("Detected " + detectedFaces.size() + " faces on the picture");
                return null;
            }
            else {
                return detectedFaces.get(0);
            }
        }
        catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }
}
