package org.example;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddBookPage extends JFrame {
    private JTextField titleField;
    private JTextField authorField;
    private BooksDataPage booksDataPage;

    public AddBookPage(BooksDataPage booksDataPage) {
        this.booksDataPage = booksDataPage;

        setTitle("Add Book");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField();
        panel.add(titleLabel);
        panel.add(titleField);

        JLabel authorLabel = new JLabel("Author:");
        authorField = new JTextField();
        panel.add(authorLabel);
        panel.add(authorField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();

            if (!title.isEmpty() && !author.isEmpty()) {
                try {
                    addBook(title, author);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Failed to add book: " + ioException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Title and Author cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(addButton);

        add(panel);
        setVisible(true);
    }

    private void addBook(String title, String author) throws IOException {
        Map<String, String> bookData = new HashMap<>();
        bookData.put("title", title);
        bookData.put("author", author);

        Gson gson = new Gson();
        String json = gson.toJson(bookData);

        NetworkUtil.doPost("/add-book", json);

        // Refresh the table in BooksDataPage
        booksDataPage.fetchDataAndPopulateTable();
        dispose();
    }
}
