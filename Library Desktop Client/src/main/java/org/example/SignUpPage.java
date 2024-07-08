package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SignUpPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public SignUpPage() {
        setTitle("Sign Up");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                try {
                    String response = NetworkUtil.doPost("/signup_admin", "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}");
                    if (response.contains("User registered")) {
                        JOptionPane.showMessageDialog(SignUpPage.this, "Sign Up successful", "Sign Up Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose(); // Close sign up page after success
                    } else {
                        JOptionPane.showMessageDialog(SignUpPage.this, "Sign Up failed", "Sign Up Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(SignUpPage.this, "Failed to connect to server", "Sign Up Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(signUpButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panel.add(cancelButton);

        add(panel);
        setVisible(true);
    }
}
