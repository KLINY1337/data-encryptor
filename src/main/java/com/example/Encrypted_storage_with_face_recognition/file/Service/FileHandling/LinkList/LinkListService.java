package com.example.Encrypted_storage_with_face_recognition.file.Service.FileHandling.LinkList;


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

@Service
@PropertySource("application.properties")
public class LinkListService {

    private List<Link> linkList;
    private final String linkListFilename;

    /**

     * Constructs a LinkListService with the specified links list filename.

     * @param linksListFilename The filename of the links list.
     */
    public LinkListService(@Value("${links.list.filename}") String linksListFilename){

        this.linkListFilename = linksListFilename;

        if (Files.exists(Path.of(linksListFilename))){

            fillLinkList();
        }
        else {

            linkList = new ArrayList<>();
        }
    }

    /**

     * Fills the link list by reading serialized Link objects from the specified file.
     * @throws RuntimeException if an error occurs while filling the link list
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     */
    private void fillLinkList() {

        try {

            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(linkListFilename));

            linkList = (List<Link>) objectInputStream.readObject();

            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {

            throw new RuntimeException(e);
        }
    }

    /**

     * Adds a new Link to the link list with the specified decrypted file name and encrypted file name.
     * @param decryptedFileName The name of the decrypted file to be added to the link list.
     * @param encryptedFileName The name of the encrypted file to be added to the link list.
     */
    public void addLinkToList(String decryptedFileName, String encryptedFileName){

        Link link = new Link(decryptedFileName, encryptedFileName);

        linkList.add(link);
    }

    /**

     * Returns the current link list.
     * @return The list of links.
     */
    public List<Link> getLinkList() {

        return linkList;
    }

    /**

     * Checks if the link list exists.
     * @return {@code true} if the link list is not empty, {@code false} otherwise.
     */
    public boolean isLinkListExists() {

        return !linkList.isEmpty();
    }

    /**

     * Writes the current link list to a file before the LinkListService is destroyed.
     * @throws RuntimeException if an error occurs while writing the link list to the file
     * @throws IOException if an I/O error occurs
     */
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
