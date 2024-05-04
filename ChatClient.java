/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatroom;

/**
 *
 * @author kavir
 */
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {

    BufferedReader in;
    PrintWriter out;
    JFrame frame = new JFrame("Chat Application");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8,40);

    public ChatClient(){

        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField,"North");
        frame.getContentPane().add(new JScrollPane(messageArea),"Center");
        frame.pack();

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });

    }

    private String getServerAddress(){
        return JOptionPane.showInputDialog(
                frame,
                "Enter the IP address of the Server : ",
                "Welcome to Chat App by Kavindu ",
                JOptionPane.QUESTION_MESSAGE
        );
    }

    private String getName(){
        return JOptionPane.showInputDialog(
                frame,
                "Choose a Screen name : ",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private void run() throws IOException{

        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress,1111);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        while (true){
            String line = in.readLine();

            if (line.startsWith("SUBMITNAME")){
                out.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            } else if (line.startsWith("MESSEGE")) {
                messageArea.append(line.substring(8) + "\n");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}
