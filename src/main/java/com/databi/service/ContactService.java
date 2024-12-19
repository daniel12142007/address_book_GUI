package com.databi.service;


import com.databi.model.Contact;
import com.databi.model.enums.Search;
import com.databi.repository.FileRepository;
import com.databi.repository.FileRepositoryImpl;

import java.util.*;

public class ContactService {
    private final FileRepository fileRepository;
    private final HashMap<String, Contact> contacts;
    private final Set<String> uniquePhone;

    public ContactService(String filPath) {
        fileRepository = new FileRepositoryImpl(filPath);
        contacts = fileRepository.getMapFormat();
        uniquePhone = fileRepository.getSetFormat();
    }

    public void createContact(Contact contact) {
        Contact save = new Contact(contact.getName(), contact.getSurname(), contact.getPhone());
        int oldSizeSet = uniquePhone.size();
        String phone = save.getPhone();
        uniquePhone.add(phone);
        if (oldSizeSet == uniquePhone.size()) {
            System.err.println("Already contact phone number");
            return;
        }
        contacts.put(phone, save);
        fileRepository.saveContact(contacts);
    }

    public Contact searchContact(String phoneNumber) {
        return contacts.get(phoneNumber);
    }

    public List<Contact> searchContactUniversal(String search,
                                                Search searchEnum) {
        return contacts.values().stream()
                .filter(a -> a.getValue(searchEnum).startsWith(search)).toList();
    }

    public boolean deleteContactByPhone(String phoneNumber) {
        if (contacts.get(phoneNumber) == null) {
            System.err.println("Not found contact");
            return false;
        }
        contacts.remove(phoneNumber);
        uniquePhone.remove(phoneNumber);
        fileRepository.saveContact(contacts);
        return true;
    }

    public List<Contact> list() {
        return new ArrayList<>(contacts.values());
    }

    public void updateContact(Contact contact,
                              String phoneNumber) {
        if (!contact.getPhone().equals(phoneNumber)
                && contacts.get(contact.getPhone()) != null) {
            System.err.println("Phone must unique phone number");
            return;
        }
        if (contacts.get(phoneNumber) == null) {
            System.err.println("Not found contact");
            return;
        }
        if (contact.getPhone().equals(phoneNumber)) {
            contacts.put(phoneNumber, contact);
            fileRepository.saveContact(contacts);
        } else {
            deleteContactByPhone(phoneNumber);
            createContact(contact);
        }
    }
}