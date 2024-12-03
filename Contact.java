import java.util.ArrayList;
import java.util.List;

public class Contact {
    private String id;
    private String name;
    private String phoneNumber;
    private List<Sms> chatHistory; // List to store message history

    public Contact(String id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.chatHistory = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Sms> getChatHistory() {
        return chatHistory;
    }

    // Add a message to chat history
    public void addMessage(Sms message) {
        chatHistory.add(message);
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Phone Number: " + phoneNumber;
    }
}
