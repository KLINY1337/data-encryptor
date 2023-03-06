package com.example.Encrypted_storage_with_face_recognition.Model.Modules.Get_face;

import com.github.sarxos.webcam.Webcam;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Setter
@Getter
@Slf4j
public class FaceGetter {

    Webcam webcam = Webcam.getDefault();

    public BufferedImage getFaceImage(){
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
