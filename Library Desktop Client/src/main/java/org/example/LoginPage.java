package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        panel.add(usernameLabel);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        panel.add(passwordLabel);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                try {
                    String response = NetworkUtil.doPost("/login", "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}");
                    if (response.contains("Invalid password") || response.contains("User not found")) {
                        JOptionPane.showMessageDialog(LoginPage.this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        int accessLevel = parseAccessLevel(response);
                        if (accessLevel == 1) {
                            JOptionPane.showMessageDialog(LoginPage.this, "Login successful as admin", "Login Success", JOptionPane.INFORMATION_MESSAGE);
                            openBooksDataPage(username); // Open BooksDataPage
                        } else {
                            JOptionPane.showMessageDialog(LoginPage.this, "You do not have admin access", "Login Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LoginPage.this, "Failed to connect to server", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(loginButton);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignUpPage();
            }
        });
        panel.add(signUpButton);

        add(panel);
        setVisible(true);
    }

    private int parseAccessLevel(String json) {
        // Manually parse the access_level from the JSON response
        int accessLevel = 0;
        String accessLevelKey = "\"access_level\":";
        int index = json.indexOf(accessLevelKey);
        if (index != -1) {
            int startIndex = index + accessLevelKey.length();
            int endIndex = json.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = json.indexOf("}", startIndex);
            }
            if (endIndex != -1) {
                try {
                    accessLevel = Integer.parseInt(json.substring(startIndex, endIndex).trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return accessLevel;
    }

    private void openBooksDataPage(String username) {
        new BooksDataPage();
        dispose();
    }

    private void openSignUpPage() {
        new SignUpPage();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginPage();
            }
        });
    }
}
