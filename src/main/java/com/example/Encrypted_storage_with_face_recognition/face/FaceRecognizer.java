package com.example.Encrypted_storage_with_face_recognition.face;

//import com.example.Encrypted_storage_with_face_recognition.Model.Modules.Face.Getting.FaceGetter;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

@Setter
@Getter
@Slf4j
@Service
public class FaceRecognizer {
    @Autowired
    private HaarCascadeDetector faceDetector;

    //@Autowired
    //private FaceGetter faceGetter;
//    public List<DetectedFace> detectFace() {
//        BufferedImage faceImage = faceGetter.getFaceImage();
//        //ImageIO.write(faceImage, "PNG", new File("hello-world.png"));
//        try {
//            assert faceImage != null;
//            List<DetectedFace> detectedFaces = faceDetector.detectFaces(ImageUtilities.createFImage(faceImage));
//
//            if (detectedFaces.size() != 1){
//                log.error("Detected " + detectedFaces.size() + " faces on the picture");
//                return null;
//            }
//            else {
//
//                ImageIO.write(ImageUtilities.createBufferedImage(detectedFaces.get(0).getFacePatch()), "PNG", new File("hello-world2.png"));
//                return detectedFaces;
//            }
//        }
//        catch (Exception e){
//            log.error(e.getMessage());
//            return null;
//        }
//    }

    public double compareFaceWithGroundTruth(List<DetectedFace> detectedFaces){
        //get decrypted list of registered faces
        //Matcher match
        //compare detectedFaces (which size is 1) to all the registered faces and return the average score of comparing
        //encrypt using salsa20
        return 0;
    }
}
