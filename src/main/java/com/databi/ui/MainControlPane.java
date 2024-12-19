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
import javafx.scene.layout.VBox;

import java.util.List;

public class MainControlPane extends GridPane {
    ContactService contactService;

    VBox vBox = new VBox();
    TableView<Contact> tableView = new TableView<>();

    public MainControlPane() {

        Button buttonUpdateTable = new Button("Update table");
        buttonUpdateTable.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-background-color: linear-gradient(to bottom, #64b5f6, #2196f3);" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 10;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        );

        contactService =
                new ContactService("C:\\Users\\User\\IdeaProjects\\address_book_GUI\\contacts.json");

        // Форма для контакта
        ContactForm contactPane = new ContactForm(
                new Contact(),
                contactService
        );
        contactPane.setStyle(
                "-fx-border-color: #2196f3;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 8;" +
                        "-fx-padding: 15;" +
                        "-fx-background-color: #e3f2fd;" +
                        "-fx-background-radius: 8;"
        );

        TableColumn<Contact, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        TableColumn<Contact, String> surnameColumn = new TableColumn<>("Surname");
        surnameColumn.setCellValueFactory(
                new PropertyValueFactory<>("surname"));

        TableColumn<Contact, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(
                new PropertyValueFactory<>("phone"));

        tableView.getColumns().addAll(
                nameColumn,
                surnameColumn,
                phoneColumn);

        tableView.setStyle(
                "-fx-border-color: #90caf9;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-color: #ffffff;" +
                        "-fx-background-radius: 8;"
        );

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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
                        if (!selectedItems.isEmpty()) {
                            contactPane.setContact(selectedItems.get(0));
                        }
                    }
                });

        buttonUpdateTable.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> updateTable());

        vBox.setSpacing(10);
        vBox.setStyle(
                "-fx-padding: 15;" +
                        "-fx-background-color: #f1f8e9;" +
                        "-fx-border-color: #c8e6c9;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );
        vBox.getChildren().addAll(buttonUpdateTable, tableView);

        // HBox - контейнер для таблицы и формы
        HBox hBox = new HBox();
        hBox.setSpacing(15);
        hBox.setStyle(
                "-fx-padding: 20;" +
                        "-fx-background-color: #f9fbe7;" +
                        "-fx-border-color: #c5e1a5;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );
        hBox.getChildren().addAll(vBox, contactPane);

        add(hBox, 0, 0);

        setStyle(
                "-fx-padding: 30;" +
                        "-fx-background-color: #fefefe;"
        );
    }

    private void updateTable() {
        List<Contact> contactList = contactService.list();
        tableView.getItems().clear();
        tableView.getItems().addAll(contactList);
    }
}
