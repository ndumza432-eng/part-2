/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package poejava;

import java.util.Random;
import java.util.Scanner;

class User {
    String firstName;
    String lastName;
    String username;
    String password;
    String cellphone;

    public User(String firstName, String lastName,
                String username, String password,
                String cellphone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellphone = cellphone;
    }
}

class Message {
    String messageID;
    int messageNumber;
    String sender;
    String recipient;
    String messageText;
    String messageHash;

    public Message(String messageID, int messageNumber,
                   String sender, String recipient,
                   String messageText, String messageHash) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.sender = sender;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = messageHash;
    }
}

public class poejava {
    static Scanner scanner = new Scanner(System.in);

    // Arrays & Counters for Part 3
    static Message[] sentMessages = new Message[100];
    static Message[] storedMessages = new Message[100];
    static Message[] disregardedMessages = new Message[100];
    static String[] messageHashes = new String[100];
    static String[] messageIDs = new String[100];

    static int sentCount = 0;
    static int storedCount = 0;
    static int disregardedCount = 0;
    static int hashCount = 0;
    static int idCount = 0;
    static int messagesSent = 0;

    public static void main(String[] args) {
        // === REGISTER ===
        System.out.println("=== REGISTER ===");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.println(checkUsername(username));
        if (!isUsernameValid(username)) return;
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.println(checkPassword(password));
        if (!isPasswordValid(password)) return;
        System.out.print("Enter cellphone number (+country code): ");
        String cellphone = scanner.nextLine();
        System.out.println(checkCellPhone(cellphone));
        if (!isCellPhoneValid(cellphone)) return;

        User user = new User(firstName, lastName, username, password, cellphone);
        System.out.println("\n✔ Registration successful!");

        // === LOGIN ===
        System.out.println("\n=== LOGIN ===");
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();
        if (!loginUser(user, loginUsername, loginPassword)) {
            System.out.println("\n❌ Login failed");
            return;
        }
        System.out.println("\n✔ Login successful");
        System.out.println("Welcome to QuickChat");

        // === MENU ===
        while (true) {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Send Message");
            System.out.println("2. Show Sent Messages");
            System.out.println("3. Quit");
            System.out.println("4. Stored Messages");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1: sendMessageFlow(user.username); break;
                case 2: showSentMessages(); break;
                case 3:
                    System.out.println("\nTotal Messages Sent: " + sentCount);
                    System.out.println("Goodbye!");
                    return;
                case 4: storedMessagesMenu(); break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    // === Send Message Flow ===
    public static void sendMessageFlow(String username) {
        System.out.print("Enter recipient number (+country code): ");
        String recipient = scanner.nextLine();
        if (!isRecipientValid(recipient)) {
            System.out.println("❌ Recipient number incorrectly formatted.");
            return;
        }
        System.out.print("Enter message: ");
        String messageText = scanner.nextLine();
        if (messageText.length() > 250) {
            System.out.println("❌ Message too long (max 250 chars).");
            return;
        }
        String messageID = generateMessageID();
        String messageHash = createMessageHash(messageID, messagesSent + 1, messageText);

        Message message = new Message(messageID, messagesSent + 1,
                                      username, recipient, messageText, messageHash);
        messagesSent++;

        int choice = sentMessage();
        if (choice == 1) {
            sentMessages[sentCount++] = message;
            messageHashes[hashCount++] = message.messageHash;
            messageIDs[idCount++] = message.messageID;
            System.out.println("✔ Message successfully sent.");
        } else if (choice == 2) {
            storedMessages[storedCount++] = message;
            messageHashes[hashCount++] = message.messageHash;
            messageIDs[idCount++] = message.messageID;
            System.out.println("✔ Message successfully stored.");
        } else {
            disregardedMessages[disregardedCount++] = message;
            System.out.println("❌ Message disregarded.");
        }
    }

    public static int sentMessage() {
        System.out.println("\nChoose option:");
        System.out.println("1. Send Message");
        System.out.println("2. Store Message");
        System.out.println("0. Disregard Message");
        return scanner.nextInt();
    }

    // === Show Sent Messages ===
    public static void showSentMessages() {
        if (sentCount == 0) {
            System.out.println("No sent messages.");
            return;
        }
        for (int i = 0; i < sentCount; i++) {
            System.out.println("\nMessage ID: " + sentMessages[i].messageID);
            System.out.println("Sender: " + sentMessages[i].sender);
            System.out.println("Recipient: " + sentMessages[i].recipient);
            System.out.println("Text: " + sentMessages[i].messageText);
        }
    }

    // === Stored Messages Menu ===
    public static void storedMessagesMenu() {
        System.out.println("\n=== STORED MESSAGES ===");
        System.out.println("1. Display Sender and Recipient");
        System.out.println("2. Display Longest Message");
        System.out.println("3. Search Message ID");
        System.out.println("4. Search Recipient");
        System.out.println("5. Delete Message");
        System.out.println("6. Display Report");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1: displaySenderAndRecipient(); break;
            case 2: displayLongestMessage(); break;
            case 3:
                System.out.print("Enter Message ID: ");
                searchMessageID(scanner.nextLine());
                break;
            case 4:
                System.out.print("Enter Recipient: ");
                searchRecipient(scanner.nextLine());
                break;
            case 5:
                System.out.print("Enter Message Hash: ");
                deleteMessage(scanner.nextLine());
                break;
            case 6: displayReport(); break;
            default: System.out.println("Invalid option.");
        }
    }

    // === Helper Methods ===
    public static void displaySenderAndRecipient() {
        if (storedCount == 0) {
            System.out.println("No stored messages.");
            return;
        }
        for (int i = 0; i < storedCount; i++) {
            System.out.println("Message " + storedMessages[i].messageNumber);
            System.out.println("Sender   : " + storedMessages[i].sender);
            System.out.println("Recipient: " + storedMessages[i].recipient);
            System.out.println("-----------------------------");
        }
    }

    public static void displayLongestMessage() {
        if (storedCount == 0) {
            System.out.println("No stored messages.");
            return;
        }
        Message longest = storedMessages[0];
        for (int i = 1; i < storedCount; i++) {
            if (storedMessages[i].messageText.length() > longest.messageText.length()) {
                longest = storedMessages[i];
            }
        }
        System.out.println("\n=== LONGEST STORED MESSAGE ===");
        System.out.println("Sender   : " + longest.sender);
        System.out.println("Recipient: " + longest.recipient);
        System.out.println("Text     : " + longest.messageText);
        System.out.println("Length   : " + longest.messageText.length() + " characters");
    }

    public static void searchMessageID(String id) {
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].messageID.equals(id)) {
                System.out.println("\n=== MESSAGE FOUND ===");
                System.out.println("Sender   : " + storedMessages[i].sender);
                System.out.println("Recipient: " + storedMessages[i].recipient);
                System.out.println("Text     : " + storedMessages[i].messageText);
                return;
            }
        }
        System.out.println("Message ID not found.");
    }

public static void searchRecipient(String recipient){

    for(int i = 0; i < storedCount; i++){

        if(storedMessages[i].recipient.equals(recipient)){

            System.out.println(
                    storedMessages[i].messageText);
        }
    }
}

public static void deleteMessage(String hash){

    for(int i = 0; i < storedCount; i++){

        if(storedMessages[i].messageHash.equals(hash)){

            for(int j = i;
                j < storedCount - 1;
                j++){

                storedMessages[j]
                        = storedMessages[j + 1];
            }

            storedCount--;

            System.out.println(
                    "Message successfully deleted.");

            return;
        }
    }

    System.out.println("Message hash not found.");
}

public static void displayReport(){

    for(int i = 0; i < storedCount; i++){

        System.out.println(
                "\nMessage ID: "
                + storedMessages[i].messageID);

        System.out.println(
                "Message Hash: "
                + storedMessages[i].messageHash);

        System.out.println(
                "Sender: "
                + storedMessages[i].sender);

        System.out.println(
                "Recipient: "
                + storedMessages[i].recipient);

        System.out.println(
                "Message: "
                + storedMessages[i].messageText);

        System.out.println(
                "--------------------------");
    }
}

// ================= USERNAME =================
public static boolean isUsernameValid(String username) {

    return username.contains("_")
            && username.length() <= 5;
}

public static String checkUsername(String username) {

    if (isUsernameValid(username)) {

        return "✔ Username correctly formatted.";

    } else {

        return "❌ Username incorrectly formatted.";
    }
}

// ================= PASSWORD =================
public static boolean isPasswordValid(String password) {

    return password.matches(
            "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$"
    );
}

public static String checkPassword(String password) {

    if (isPasswordValid(password)) {

        return "✔ Password meets requirements.";

    } else {

        return "❌ Password does not meet requirements.";
    }
}

// ================= CELLPHONE =================
public static boolean isCellPhoneValid(String cellphone) {

    return cellphone.matches("^\\+[0-9]{9,15}$");
}

public static String checkCellPhone(String cellphone) {

    if (isCellPhoneValid(cellphone)) {

        return "✔ Cellphone number correctly formatted.";

    } else {

        return "❌ Cellphone number incorrectly formatted.";
    }
}

// ================= LOGIN =================
public static boolean loginUser(User user,
                                String username,
                                String password) {

    return user.username.equals(username)
            && user.password.equals(password);
}

// ================= RECIPIENT =================
public static boolean isRecipientValid(String recipient) {

    return recipient.matches("^\\+[0-9]{9,15}$");
}

// ================= MESSAGE ID =================
public static String generateMessageID() {

    Random random = new Random();

    int number = 100000000 + random.nextInt(900000000);

    return String.valueOf(number);
}

// ================= HASH =================
public static String createMessageHash(String messageID,
                                       int messageNumber,
                                       String message) {

    String[] words = message.split(" ");

    String firstWord = words[0];
    String lastWord = words[words.length - 1];

    String firstTwoDigits = messageID.substring(0, 2);

    return (
            firstTwoDigits
            + ":"
            + messageNumber
            + ":"
            + firstWord
            + lastWord
    ).toUpperCase();
}
}