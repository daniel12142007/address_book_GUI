package server;

import com.databi.model.Command;
import com.databi.model.Contact;
import com.databi.model.Response;
import com.databi.model.enums.Status;
import com.databi.service.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private final static int PORT = 3333;
    private final static String FILE_PATH = "C:\\Users\\User\\IdeaProjects\\address_book_GUI\\contacts.json";
    private final static ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final static ObjectMapper objectMapper = new ObjectMapper();


    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server start");

            System.out.println("Waiting");
            System.out.println(serverSocket.getInetAddress());
            System.out.println(serverSocket.getLocalPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(
                        () -> {
                            try {
                                handlerClient(clientSocket);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handlerClient(Socket clientSocket) throws IOException {
        ContactService contactService = new ContactService(FILE_PATH);
//                input
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream())
        );
//                output
        PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);

        String request = reader.readLine();
        System.out.println("Accept " + request);
        Command command = objectMapper.readValue(request, Command.class);
        Contact contact = command.contact();
        switch (command.command()) {
            case LIST_COMMAND -> {
                List<Contact> contacts = contactService.list();

                Response response = new Response(Status.OK, contacts);
                objectMapper.writeValue(printWriter, response);
            }
            case NEW_COMMAND -> {
                contactService.createContact(contact);
                System.out.println("Saved contact: " + contact);

                Response response = new Response(Status.OK, null);
                objectMapper.writeValue(printWriter, response);
            }
            case UPDATE_COMMAND -> {
                contactService.updateContact(contact, contact.getPhone());
                System.out.println("Updated contact");

                Response response = new Response(Status.OK, null);
                objectMapper.writeValue(printWriter, response);
            }
            case DELETE_COMMAND -> {
                contactService.deleteContactByPhone(contact.getPhone());
                System.out.println("Deleted contact");

                Response response = new Response(Status.OK, null);
                objectMapper.writeValue(printWriter, response);
            }
            case FIND_COMMAND -> {
                Contact contactFound = contactService.searchContact(contact.getPhone());
                System.out.println("Contact found: " + contactFound);
                Response response;
                if (contactFound == null)
                    response = new Response(Status.ERROR, null);
                else
                    response = new Response(Status.OK, List.of(contactFound));
                objectMapper.writeValue(printWriter, response);
            }
            default -> System.err.println("Unknown command");
        }
    }
}