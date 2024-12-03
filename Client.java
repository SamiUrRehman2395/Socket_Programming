import java.io.*;
import java.net.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private List<Contact> contacts = new ArrayList<>();
    private List<BlockList> blockedContacts = new ArrayList<>();

    public Client(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            preAddContacts();
            new Thread(this).start(); // Start client thread
        } catch (IOException e) {
            System.out.println("Could not connect to server at " + serverAddress + ":" + port);
            e.printStackTrace();
        }
    }

    private void preAddContacts() {
        contacts.add(new Contact("1", "Sami", "03039812367"));

    }


    private void displayMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Manage Contacts");
        System.out.println("2. Send Message");
        System.out.println("3. View Chat History");
        System.out.println("4. Exit");
        System.out.print("Enter choice: ");
    }

    private void handleMainMenuChoice(int choice, Scanner scanner) {
        switch (choice) {
            case 1 -> manageContacts(scanner);
            case 2 -> sendMessageMenu(scanner);
            case 3 -> viewChatHistoryMenu(scanner);
            case 4 -> System.exit(0);
            default -> System.out.println("Invalid choice. Try again.");
        }
    }

    private void viewChatHistoryMenu(Scanner scanner) {
        viewAllContacts();
        System.out.print("Enter Contact ID to view chat history: ");
        String id = scanner.nextLine();
        Contact contact = findContactById(id);
        if (contact != null) {
            viewChatHistory(contact);
        } else {
            System.out.println("Contact not found.");
        }
    }

    private void viewChatHistory(Contact contact) {
        System.out.println("\n--- Chat History with " + contact.getName() + "---");
        if (contact.getChatHistory().isEmpty()) {
            System.out.println("No messages with this contact.");
        } else {
            contact.getChatHistory().stream()
                    .sorted((m1, m2) -> m2.getTimestamp().compareTo(m1.getTimestamp()))
                    .forEach(message -> {
                        String senderLabel = message.getSender().equals("Me") ? "(Me)" : contact.getName();
                        System.out.println("[" + message.getTimestamp().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "] " + senderLabel + ": " + message.getContent());
                    });
        }
    }

    private void manageContacts(Scanner scanner) {
        while (true) {
            displayManageContactsMenu();
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> addContact(scanner);
                case 2 -> deleteContact(scanner);
                case 3 -> viewAllContacts();
                case 4 -> manageBlockList(scanner);
                case 5 -> { return; }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void displayManageContactsMenu() {
        System.out.println("\n--- Manage Contacts ---");
        System.out.println("1. Add Contact");
        System.out.println("2. Delete Contact");
        System.out.println("3. View All Contacts");
        System.out.println("4. Manage Block List");
        System.out.println("5. Go to Main Menu");
        System.out.print("Enter choice: ");
    }

    private void manageBlockList(Scanner scanner) {
        while (true) {
            displayManageBlockListMenu();
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> blockContact(scanner);
                case 2 -> unblockContact(scanner);
                case 3 -> viewBlockedContacts();
                case 4 -> { return; }
                case 5 -> { return; }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void displayManageBlockListMenu() {
        System.out.println("\n--- Manage Block List ---");
        System.out.println("1. Block Contact");
        System.out.println("2. Unblock Contact");
        System.out.println("3. View Block List");
        System.out.println("4. Go to Back Menu");
        System.out.println("5. Go to Main Menu");
        System.out.print("Enter choice: ");
    }

    private void sendMessageMenu(Scanner scanner) {
        while (true) {
            displaySendMessageMenu();
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> selectContactToSendMessage(scanner);
                case 2 -> startNewChat(scanner);
                case 3 -> { return; }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void displaySendMessageMenu() {
        System.out.println("\n--- Send Message ---");
        System.out.println("1. Select Contact to Send Message");
        System.out.println("2. Start New Chat");
        System.out.println("3. Go to Main Menu");
        System.out.print("Enter choice: ");
    }

    private void addContact(Scanner scanner) {
        System.out.print("Enter Contact ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Contact Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Contact Number: ");
        String number = scanner.nextLine();
        contacts.add(new Contact(id, name, number));
        System.out.println("Contact added successfully!");
    }

    private void deleteContact(Scanner scanner) {
        System.out.print("Enter Contact ID to delete: ");
        String id = scanner.nextLine();
        Contact contact = findContactById(id);
        if (contact != null) {
            contacts.remove(contact);
            System.out.println("Contact deleted successfully!");
        } else {
            System.out.println("Contact not found.");
        }
    }

    private Contact findContactById(String id) {
        for (Contact contact : contacts) {
            if (contact.getId().equals(id)) {
                return contact;
            }
        }
        return null;
    }

    private void blockContact(Scanner scanner) {
        System.out.print("Enter Contact ID to block: ");
        String id = scanner.nextLine();
        Contact contact = findContactById(id);
        if (contact != null) {
            blockedContacts.add(new BlockList(contact.getId(), contact.getName(), contact.getPhoneNumber()));
            System.out.println("Contact blocked successfully!");
        } else {
            System.out.println("Contact not found.");
        }
    }

    private void unblockContact(Scanner scanner) {
        System.out.print("Enter Contact ID to unblock: ");
        String id = scanner.nextLine();
        blockedContacts.removeIf(blocked -> blocked.getId().equals(id));
        System.out.println("Contact unblocked successfully!");
    }

    private void viewAllContacts() {
        System.out.println("Contacts:");
        if (contacts.isEmpty()) {
            System.out.println("No contacts to display.");
        } else {
            for (Contact contact : contacts) {
                System.out.println(contact);
            }
        }
    }

    private void viewBlockedContacts() {
        System.out.println("Blocked Contacts:");
        if (blockedContacts.isEmpty()) {
            System.out.println("No contacts are currently blocked.");
        } else {
            for (BlockList blocked : blockedContacts) {
                System.out.println(blocked);
            }
        }
    }

    private void selectContactToSendMessage(Scanner scanner) {
        viewAllContacts();
        System.out.print("Enter Contact ID to send message: ");
        String id = scanner.nextLine();
        Contact contact = findContactById(id);
        if (contact != null) {
            enterMessagingMode(scanner, contact);
        } else {
            System.out.println("Contact not found.");
        }
    }

    private void startNewChat(Scanner scanner) {
        System.out.print("Enter New Contact Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter New Contact Number: ");
        String number = scanner.nextLine();
        String id = String.valueOf(contacts.size() + 1);
        Contact contact = new Contact(id, name, number);
        contacts.add(contact);
        System.out.println("New contact added successfully. Starting chat...");
        enterMessagingMode(scanner, contact);
    }

    private void enterMessagingMode(Scanner scanner, Contact contact) {
        System.out.println("Enter messages to " + contact.getName() + " (enter '0' to exit messaging mode):");
        while (true) {
            String message = scanner.nextLine();
            if (message.equals("0")) {
                System.out.println("Exiting messaging mode...");
                break;
            }
            sendMessage(message, contact);
        }
    }

    private void sendMessage(String message, Contact contact) {
        if (out != null) {
            out.println(message);
            Sms sentMessage = new Sms(message, "Me"); // Track as sent by "Me"
            contact.addMessage(sentMessage);
        }
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            new Thread(() -> {
                try {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Server: " + inputLine);
                        Sms receivedMessage = new Sms(inputLine, "Server"); // Track as received from "Server"
                        Contact contact = findContactById("1"); // Assuming a single server
                        if (contact != null) contact.addMessage(receivedMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            while (true) {
                displayMainMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                handleMainMenuChoice(choice, scanner);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client("127.0.0.1", 12345);
    }
}
