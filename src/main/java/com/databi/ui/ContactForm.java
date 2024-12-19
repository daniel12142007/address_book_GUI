package com.databi.ui;

import com.databi.model.Contact;
import com.databi.service.ContactService;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class ContactForm extends GridPane {

    private ContactService contactService;
    private Contact contact;
    private TextField nameField = new TextField();
    private TextField surnameField = new TextField();
    private TextField phoneField = new TextField();

    public ContactForm(Contact contact, ContactService contactService) {
        this.contactService = contactService;
        this.contact = contact;
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        Label nameLabel = new Label("Name");
        Label surnameLabel = new Label("Surname");
        Label phoneLabel = new Label("Phone");

        add(nameLabel, 0, 0);
        add(nameField, 1, 0);

        add(surnameLabel, 0, 1);
        add(surnameField, 1, 1);

        add(phoneLabel, 0, 2);
        add(phoneField, 1, 2);

        add(saveButton, 0, 3);
        add(cancelButton, 1, 3);

        saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            contact.setName(nameField.getText());
            contact.setSurname(surnameField.getText());
            contact.setPhone(phoneField.getText());

            Contact oldContact = contactService.searchContact(contact.getPhone());
            if (oldContact != null) {
                System.out.println("Updating existing contact");
                contactService.updateContact(oldContact, contact.getPhone());
            } else {
                System.out.println("Creating new contact");
                contactService.createContact(contact);
            }
            nameField.clear();
            surnameField.clear();
            phoneField.clear();
        });
    }

    public void setContact(Contact contact) {
        this.nameField.setText(contact.getName());
        this.surnameField.setText(contact.getSurname());
        this.phoneField.setText(contact.getPhone());
    }
}