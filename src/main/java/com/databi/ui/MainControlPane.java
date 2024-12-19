package com.databi.ui;

import com.databi.model.Contact;
import com.databi.service.ContactService;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class MainControlPane extends GridPane {
    ContactService contactService;

    HBox hBox = new HBox();
    TableView<Contact> tableView = new TableView();


    public MainControlPane() {

        Button buttonUpdateTable = new Button("Update table");

        contactService =
                new ContactService("C:\\Users\\User\\IdeaProjects\\address_book_GUI\\contacts.json");

        ContactForm contactPane = new ContactForm(
                new Contact(),
                contactService
        );

        FindContactPane findContactPane = new FindContactPane(
                contactService
        );

        TableColumn<Contact, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        TableColumn<Contact, String> surnameColumn = new TableColumn<>("Surname");
        surnameColumn.setCellValueFactory(
                new PropertyValueFactory<>("surname"));

        TableColumn<Contact, String> phoneColumn = new TableColumn<>("phone");
        phoneColumn.setCellValueFactory(
                new PropertyValueFactory<>("phone"));


        tableView.getColumns().addAll(
                nameColumn,
                surnameColumn,
                phoneColumn);

        TableView.TableViewSelectionModel<Contact> selectionModel =
                tableView.getSelectionModel();

        selectionModel.setSelectionMode(
                SelectionMode.SINGLE);

        updateTable();

        ObservableList<Contact> selectedItems =
                selectionModel.getSelectedItems();

        selectedItems.addListener(
                new ListChangeListener<Contact>() {
                    @Override
                    public void onChanged(
                            Change<? extends Contact> change) {
                        contactPane.setContact(selectedItems.getFirst());
                    }
                });

        buttonUpdateTable.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            updateTable();
        });

        hBox.getChildren().add(tableView);
        hBox.getChildren().add(contactPane);

        add(hBox, 0, 0);
        add(buttonUpdateTable, 1,0);
    }

    private void updateTable() {
        List<Contact> contactList = contactService.list();
        tableView.getItems().clear();
        for (Contact contact : contactList) {
            tableView.getItems().add(contact);
        }
    }
}