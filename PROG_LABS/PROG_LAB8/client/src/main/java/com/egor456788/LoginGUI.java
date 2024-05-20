package com.egor456788;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.io.*;
import java.util.ResourceBundle;

public class LoginGUI {


    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;




    private ResourceBundle bundle;


    public LoginGUI() {

        this.bundle = ResourceBundle.getBundle("messages", Applicaton.locale);

        createUI();
        //connectToServer();
    }

    private void createUI() {
        frame = new JFrame(bundle.getString("status"));
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel(bundle.getString("username"));
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel(bundle.getString("password"));
        passwordField = new JPasswordField();
        JButton loginButton = new JButton(bundle.getString("login"));
        JButton registerButton = new JButton(bundle.getString("register"));
        statusLabel = new JLabel("");

        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(loginButton);
        frame.add(registerButton);
        frame.add(statusLabel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticate("login_alter");
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticate("register_alter");
            }
        });

        frame.setVisible(true);
    }


    private void authenticate(String command) {
        try {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String hashedPassword = Applicaton.hashPassword(password);

            String receivedMessage = Sender.sendToServer(new Request(command, Applicaton.clientPort, username, hashedPassword, -1));
            if (receivedMessage != null) {
                statusLabel.setText(bundle.getString("connection_error"));
            } else if (receivedMessage.contains("успешно")) {
                statusLabel.setText(bundle.getString("status") + ": " + bundle.getString("login_successful"));
                // Handle successful login (e.g., open the main application window)
            } else {
                statusLabel.setText(bundle.getString("status") + ": " + receivedMessage);
            }

        } catch (Exception e) {
            statusLabel.setText(bundle.getString("status") + ": Error: " + e.getMessage());
        }
    }


}
