package br.edu.ifsuldeminas.sd.chat.gui;

import br.edu.ifsuldeminas.sd.chat.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatGUI extends JFrame implements MessageContainer {
    private static final long serialVersionUID = 1L;
    private JTextPane chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private Sender sender;
    private String from;
    private int localPort;
    private int serverPort;

    public ChatGUI(String serverName, int serverPort, int localPort, String from) {
        this.from = from;
        this.localPort = localPort;
        this.serverPort = serverPort;

        setTitle("Chat");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Informações fixas no topo
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(7, 94, 84));
        infoPanel.setForeground(Color.WHITE);

        JLabel nameLabel = new JLabel("Nome: " + from);
        nameLabel.setForeground(Color.WHITE);
        JLabel localPortLabel = new JLabel("Porta Local: " + localPort);
        localPortLabel.setForeground(Color.WHITE);
        JLabel serverPortLabel = new JLabel("Porta Remota: " + serverPort);
        serverPortLabel.setForeground(Color.WHITE);

        infoPanel.add(nameLabel);
        infoPanel.add(localPortLabel);
        infoPanel.add(serverPortLabel);

        add(infoPanel, BorderLayout.NORTH);

        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setBackground(new Color(237, 237, 237));
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatArea);

        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));
        sendButton = new JButton("Send");
        sendButton.setBackground(new Color(7, 94, 84));
        sendButton.setForeground(Color.WHITE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(messageField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        try {
            sender = ChatFactory.build(serverName, serverPort, localPort, this);
        } catch (ChatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error starting chat: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        setVisible(true);
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            String formattedMessage = String.format("%s%s%s", from, MessageContainer.FROM, message);
            try {
                sender.send(formattedMessage);
                appendMessage(String.format("%s > %s", from, message), new Color(200, 255, 200), Color.BLACK, StyleConstants.ALIGN_RIGHT); // Verde claro
                messageField.setText("");
            } catch (ChatException e) {
                JOptionPane.showMessageDialog(this, "Error sending message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void newMessage(String message) {
        String[] messageParts = message.split(MessageContainer.FROM);
        if (messageParts.length == 2) {
            appendMessage(messageParts[0] + " > " + messageParts[1].trim(), new Color(100, 150, 100), Color.WHITE, StyleConstants.ALIGN_LEFT); // Verde escuro
        }
    }

    private void appendMessage(String message, Color bgColor, Color fgColor, int alignment) {
        StyledDocument doc = chatArea.getStyledDocument();
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(attributeSet, fgColor);
        StyleConstants.setBackground(attributeSet, bgColor);
        StyleConstants.setAlignment(attributeSet, alignment);
        StyleConstants.setFontSize(attributeSet, 14);
        StyleConstants.setFontFamily(attributeSet, "Arial");

        try {
            int len = doc.getLength();
            doc.insertString(len, message + "\n", attributeSet);
            doc.setParagraphAttributes(len, message.length(), attributeSet, false);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        chatArea.setCaretPosition(doc.getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String serverName = "localhost";
                int localPort = Integer.parseInt(JOptionPane.showInputDialog("Enter local port:"));
                int serverPort = Integer.parseInt(JOptionPane.showInputDialog("Enter server port:"));
                String from = JOptionPane.showInputDialog("Enter your name:");
                new ChatGUI(serverName, serverPort, localPort, from);
            }
        });
    }
}
