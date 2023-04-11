package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.LinkList;


import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Slf4j
@Service
@PropertySource("application.properties")
public class LinkListService {

    private List<Link> linkList;
    private String linkListFilename;

    public LinkListService(@Value("${links.list.filename}") String linksListFilename){
        this.linkListFilename = linksListFilename;

        if (Files.exists(Path.of(linksListFilename))){
            fillLinkList();
        }
        else {
            linkList = new ArrayList<>();
        }
    }

    private void fillLinkList() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(linkListFilename));

            linkList = (List<Link>) objectInputStream.readObject();

            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void addLinkToList(String decryptedFileName, String encryptedFileName){
        Link link = new Link(decryptedFileName, encryptedFileName);

        linkList.add(link);
    }

    @PreDestroy
    public void writeLinkListToFile(){
        File file = new File(linkListFilename);

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));

            objectOutputStream.writeObject(linkList);

            objectOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
