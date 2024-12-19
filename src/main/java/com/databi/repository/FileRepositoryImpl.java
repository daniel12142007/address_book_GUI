package com.databi.repository;

import com.databi.model.Contact;
import com.databi.util.FileReaderThread;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileRepositoryImpl implements FileRepository {
    private final String PATH_FILE;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Contact> contacts = readFile();

    public FileRepositoryImpl(String filePath) {
        this.PATH_FILE = filePath;
    }

    @Override
    public void saveContact(HashMap<String, Contact> contacts) {
        try {
            objectMapper.writeValue(new File(PATH_FILE), new ArrayList<>(contacts.values()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Contact> readFile() {
        try {
            FileReaderThread fileReaderThread = new FileReaderThread(PATH_FILE);
            Thread thread = new Thread(fileReaderThread);

            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                thread.interrupt();
                throw new RuntimeException(e);
            }
            System.out.println(fileReaderThread.getContacts());
            return fileReaderThread.getContacts();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public LinkedHashSet<String> getSetFormat() {
        return readFile().stream().map(Contact::getPhone)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public HashMap<String, Contact> getMapFormat() {
        HashMap<String, Contact> contactHashMap = new LinkedHashMap<>();
        for (Contact contact : readFile())
            contactHashMap.put(contact.getPhone(), contact);
        return contactHashMap;
    }
}