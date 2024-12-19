package com.databi.repository;


import com.databi.model.Contact;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

public interface FileRepository {
    void saveContact(HashMap<String, Contact> contacts);

    List<Contact> readFile() throws InterruptedException;

    LinkedHashSet<String> getSetFormat();

    HashMap<String, Contact> getMapFormat();
}