import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sms {
    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;
    private String sender; // Added sender field to track message origin
    private static int idCounter = 1;
    private String messageId;

    public Sms(String content, String sender) {
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
        this.sender = sender;
        this.messageId = "MSG" + String.format("%03d", idCounter++);
    }

    public void markAsRead() {
        this.isRead = true;
    }

    public String getMessageId() {
        return messageId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public boolean isRead() {
        return isRead;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return "[" + timestamp.format(dateFormatter) + "] " + sender + ": " + content;
    }
}
