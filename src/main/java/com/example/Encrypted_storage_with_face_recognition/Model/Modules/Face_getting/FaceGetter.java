package com.example.Encrypted_storage_with_face_recognition.Model.Modules.Face_getting;

import com.github.sarxos.webcam.Webcam;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;

@Setter
@Getter
@Slf4j
public class FaceGetter {

    static Webcam webcam = Webcam.getDefault();

    public static BufferedImage getFaceImage(){
        try {
            webcam.open();
            return webcam.getImage();
        }
        catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
        finally {
            webcam.close();
        }
    }
}
