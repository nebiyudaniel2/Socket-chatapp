package chatapp;

import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class ChatUI extends Application {
    private ChatClient client = new ChatClient();
    private TextArea chatArea;
    private TextField messageField;

    @Override
    public void start(Stage stage) {
        // --- Login Dialog ---
        TextInputDialog loginDialog = new TextInputDialog("User");
        loginDialog.setTitle("Join Chat");
        loginDialog.setHeaderText("Enter your username:");
        loginDialog.setContentText("Username:");

        String username = loginDialog.showAndWait().orElse("Anonymous");

        // --- Connect to server ---
        try {
            client.connect("localhost", 5000, username, message ->
                    // Platform.runLater ensures UI updates happen on the JavaFX thread
                    Platform.runLater(() -> chatArea.appendText(message + "\n"))
            );
        } catch (Exception e) {
            showError("Could not connect to server. Is it running?");
            return;
        }

        // --- Build Chat Window ---
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPrefHeight(400);
        chatArea.setStyle("-fx-font-family: monospace; -fx-font-size: 13px;");

        messageField = new TextField();
        messageField.setPromptText("Type a message...");
        messageField.setPrefWidth(400);

        Button sendButton = new Button("Send");
        sendButton.setDefaultButton(true); // Enter key triggers it
        sendButton.setOnAction(e -> sendMessage());

        HBox inputRow = new HBox(8, messageField, sendButton);
        inputRow.setAlignment(Pos.CENTER);
        HBox.setHgrow(messageField, Priority.ALWAYS);

        VBox root = new VBox(10, chatArea, inputRow);
        root.setPadding(new Insets(12));

        stage.setTitle("Chat — " + username);
        stage.setScene(new Scene(root, 520, 480));
        stage.setOnCloseRequest(e -> {
            try { client.disconnect(); } catch (Exception ignored) {}
        });
        stage.show();
    }

    private void sendMessage() {
        String text = messageField.getText().trim();
        if (!text.isEmpty()) {
            client.sendMessage(text);
            chatArea.appendText("Me: " + text + "\n"); // show your own message locally
            messageField.clear();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace(); // this will print the real error in the console
        }
    }
}