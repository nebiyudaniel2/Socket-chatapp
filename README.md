# JavaFX ChatApp

A real-time multi-client chat application built with JavaFX and Java Sockets.
Users can chat with each other over a local network (LAN) or the internet.

---

## Features

- Real-time messaging between multiple clients
- Server broadcasts messages to all connected users
- Each user sets their own username on join
- Clean JavaFX GUI with send-on-Enter support
- Supports same-machine testing and LAN/internet connections

---

## Tech Stack

- Java 21+
- JavaFX 21 (GUI)
- Java Sockets (networking)
- Maven (build & dependency management)

---

## Project Structure

src/
└── main/
    └── java/
        └── chatapp/
            ├── ChatServer.java       # Server — listens on port 5000, manages clients
            ├── ClientHandler.java    # One thread per connected client
            ├── ChatClient.java       # Socket logic + background message reader
            └── ChatUI.java           # JavaFX window and login dialog

---

## Prerequisites

- JDK 21 or higher       → https://adoptium.net
- JavaFX SDK 21 LTS      → https://gluonhq.com/products/javafx
- IntelliJ IDEA          → https://www.jetbrains.com/idea
- Maven (bundled with IntelliJ)

---

## Setup & Run

### 1. Clone or download the project

    git clone https://github.com/nebiyudaniel2/chatapp.git
    cd chatapp

### 2. Open in IntelliJ IDEA

    File → Open → select the chatapp folder
    Wait for Maven to sync dependencies automatically.
    If it does not sync, click the Reload button in the Maven panel.

### 3. Run the Server

    Right-click ChatServer.java → Run 'ChatServer.main()'
    You should see: Server started on port 5000
    Leave this window running.

### 4. Run the Client

    In the Maven panel on the right:
    Plugins → javafx → double-click javafx:run
    A login dialog will appear — enter your username and click OK.

### 5. Test with multiple clients

    Run javafx:run again in a second terminal or Maven run config.
    Each instance is a separate chat participant.


## How It Works

    Client connects → sends username → server registers it
    Client sends message → server receives it via ClientHandler
    ClientHandler broadcasts the message to all other connected clients
    Each client's background thread receives the message and updates the UI

    Platform.runLater() is used to safely update the JavaFX UI
    from the background socket reader thread.

---

## Known Limitations

- No message history (messages are lost on disconnect)
- No private/direct messaging
- No encryption (messages sent as plain text)
- Server must be started before any client connects

---

## Possible Improvements

- Add timestamps to messages
- Show online users list in a sidebar
- Add private messaging between users
- Encrypt messages with TLS/SSL
- Save chat history to a file or database
- Build a login system with usernames and passwords

---

## Author

    Nebiyu Daniel 
    GitHub: https://github.com/nebiyudaniel2

---

## License

    MIT License — free to use, modify, and distribute.
