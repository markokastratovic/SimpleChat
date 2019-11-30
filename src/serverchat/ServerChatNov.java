package serverchat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import chat.AbstractChat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.FChat;

/**
 *
 * @author student1
 */
public class ServerChatNov extends AbstractChat {

    private ServerSocket serverSocket;
    private Socket socket;
    private FChat gui;
    BufferedReader serverReader;
    PrintWriter printWriter;

    public static void main(String[] args) throws Exception {
        ServerChatNov serverChat = new ServerChatNov();
        serverChat.connect();
    }

    public ServerChatNov() {
        prepareGui();
    }

    public void prepareGui() {
        gui = new FChat(this);
        gui.setVisible(true);
        gui.getBtnSend().setEnabled(false);
        gui.setTitle("Server");
    }

    private void connect() throws Exception {
        serverSocket = new ServerSocket(9000);
        System.out.println("Waiting clients...");
        socket = serverSocket.accept();
        System.out.println("Client connected");

        serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream());

        String messageFromServer = serverReader.readLine();
        if (!messageFromServer.isEmpty()) {
            gui.addRecievedMessage(messageFromServer);
        }
        gui.getBtnSend().setEnabled(true);
    }

    @Override
    public void send(String message) {
        message = "Server: " + message;
        try {
            gui.addSentMessage(message);
            gui.getBtnSend().setEnabled(false);

            printWriter.println(message);
            printWriter.flush();
            if (message.equals("end")) {
                socket.close();
                gui.dispose();
            }

            String messageFromServer = serverReader.readLine();
//            if (!messageFromServer.isEmpty()) {
//                gui.addRecievedMessage(messageFromServer);
//            }
            gui.addRecievedMessage(messageFromServer);

            gui.getBtnSend().setEnabled(true);
        } catch (IOException ex) {
            Logger.getLogger(ServerChatNov.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
