package com.databi.repository;

import com.databi.model.Contact;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileRepositoryImpl implements FileRepository {
    private final String PATH_FILE;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Object fileLock = new Object();

    public FileRepositoryImpl(String filePath) {
        this.PATH_FILE = filePath;
    }

    @Override
    public void saveContact(HashMap<String, Contact> contacts) {
        synchronized (fileLock) {
            try {
                objectMapper.writeValue(new File(PATH_FILE), new ArrayList<>(contacts.values()));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public List<Contact> readFile() {
        synchronized (fileLock) {
            try (FileReader fileReader = new FileReader(PATH_FILE)) {
                return objectMapper.readValue(
                        fileReader,
                        objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Contact.class));
            } catch (IOException e) {
                System.err.println(e.getMessage());
                return new ArrayList<>();
            }
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