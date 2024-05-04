/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatroom;

/**
 *
 * @author kavir
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class ChatServer {

    private static final int PORT = 1111;
    private static HashSet<String> names = new HashSet<String>();
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    public static void main(String[] args) throws IOException {

        System.out.println("The chat server is running");
        ServerSocket listner = new ServerSocket(PORT);

        try {
            while (true){
                Socket socket = listner.accept();
                Thread handlerThread = new Thread(new Handler(socket));
                handlerThread.start();
            }
        }finally {
            listner.close();
        }

    }

    private static class Handler implements Runnable{

        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket){
            this.socket = socket;
        }

        public void run(){
            try {

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(),true);

                while (true){
                    out.println("SUBMITNAME");
                    name = in.readLine();

                    if (name == null){
                        return;
                    }

                    if (!names.contains(names)){
                        names.add(name);
                        break;
                    }
                }

                out.println("NAMEACCEPTED");
                writers.add(out);

                while (true){
                    String input = in.readLine();
                    if (input == null){
                        return;
                    }

                    for (PrintWriter writer : writers){
                        writer.println("MESSEGE "+name+" : "+input);
                    }
                }

            }catch (Exception e){
                System.out.println(e);
            }finally {
                if (names != null){
                    names.remove(out);
                }
                try {
                    socket.close();
                }catch (IOException e){
                    System.out.println(e);
                }
            }
        }
    }
}

