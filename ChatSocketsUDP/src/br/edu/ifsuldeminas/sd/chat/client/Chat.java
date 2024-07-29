package br.edu.ifsuldeminas.sd.chat.client;
import br.edu.ifsuldeminas.sd.chat.gui.ChatGUI;
import java.util.Scanner;
import br.edu.ifsuldeminas.sd.chat.ChatException;
import br.edu.ifsuldeminas.sd.chat.ChatFactory;
import br.edu.ifsuldeminas.sd.chat.MessageContainer;
import br.edu.ifsuldeminas.sd.chat.Sender;
import br.edu.ifsuldeminas.sd.chat.gui.ChatGUI;

public class Chat {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                String serverName = "localhost";
                int localPort = Integer.parseInt(javax.swing.JOptionPane.showInputDialog("Enter local port:"));
                int serverPort = Integer.parseInt(javax.swing.JOptionPane.showInputDialog("Enter server port:"));
                String from = javax.swing.JOptionPane.showInputDialog("Enter your name:");
                new ChatGUI(serverName, serverPort, localPort, from);
            }
        });
    }
}