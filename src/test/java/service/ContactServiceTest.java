package service;

import com.databi.model.Contact;
import com.databi.model.enums.Search;
import com.databi.service.ContactService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ContactServiceTest {
    final String PHONE = "+996 705";
    final String PHONE2 = "+996 700";
    final ContactService contactService =
            new ContactService("C:\\Users\\User\\IdeaProjects\\address_book\\contacts.json");

    @Test
    @DisplayName("Test create")
    public void createTest() {
        Contact contact1 = new Contact(
                "Daniel",
                "Ahatdzhanov",
                PHONE);
        contactService.createContact(contact1);

        Contact contact2 = contactService.searchContact(PHONE);
        contactService.deleteContactByPhone(PHONE);
        assertEquals(contact1, contact2);
    }

    @Test
    @DisplayName("Test update")
    public void updateTest() {
        Contact contact1 = new Contact(
                "Daniel",
                "Ahatdzhanov",
                PHONE);
        contactService.createContact(contact1);
        String phoneUpdate = "43924";
        contactService.updateContact(
                new Contact("Update", "Update", phoneUpdate),
                PHONE);
        Contact contact = contactService.searchContact(phoneUpdate);
        contactService.deleteContactByPhone(phoneUpdate);
        assertNotEquals(contact, null);
    }

    @Test
    @DisplayName("Test delete")
    public void deleteTest() {
        Contact contact = new Contact(
                "Daniel",
                "Ahatdzhanov",
                PHONE);
        contactService.createContact(contact);

        contactService.deleteContactByPhone(PHONE);
        assertNull(contactService.searchContact(PHONE));
    }

    @Test
    @DisplayName("Test search")
    public void searchTest() {
        Contact contact1 = new Contact(
                "Daniel",
                "Ahatdzhanov",
                PHONE);
        contactService.createContact(contact1);
        Contact contact2 = new Contact(
                "Daoijgo",
                "Ahatdzhanov",
                PHONE2);
        contactService.createContact(contact2);
        assertEquals(
                contactService.searchContactUniversal("Da", Search.NAME),
                List.of(contact1, contact2)
        );
        assertEquals(
                contactService.searchContactUniversal("Ahat", Search.SURNAME),
                List.of(contact1, contact2)
        );
        contactService.deleteContactByPhone(PHONE);
        contactService.deleteContactByPhone(PHONE2);
    }
}