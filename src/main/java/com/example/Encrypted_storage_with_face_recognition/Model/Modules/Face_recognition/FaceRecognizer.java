package com.example.Encrypted_storage_with_face_recognition.Model.Modules.Face_recognition;

import com.example.Encrypted_storage_with_face_recognition.Model.Modules.Face_getting.FaceGetter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.image.processing.face.detection.benchmarking.Matcher;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Setter
@Getter
@Slf4j
public class FaceRecognizer {
    private HaarCascadeDetector faceDetector = new HaarCascadeDetector();

    public List<DetectedFace> detectFace() throws IOException {
        BufferedImage faceImage = FaceGetter.getFaceImage();
        //ImageIO.write(faceImage, "PNG", new File("hello-world.png"));
        try {
            assert faceImage != null;
            List<DetectedFace> detectedFaces = faceDetector.detectFaces(ImageUtilities.createFImage(faceImage));

            if (detectedFaces.size() != 1){
                log.error("Detected " + detectedFaces.size() + " faces on the picture");
                return null;
            }
            else {

                ImageIO.write(ImageUtilities.createBufferedImage(detectedFaces.get(0).getFacePatch()), "PNG", new File("hello-world2.png"));
                return detectedFaces;
            }
        }
        catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    public double compareFaceWithGroundTruth(List<DetectedFace> detectedFaces){
        //get decrypted list of registered faces
        //Matcher match
        //compare detectedFaces (which size is 1) to all the registered faces and return the average score of comparing
        return 0;
    }
}
