package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class BooksDataPage extends JFrame {
    private final JTable booksTable;
    private final DefaultTableModel tableModel;

    public BooksDataPage() {
        setTitle("Books Data");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"ID", "Title", "Author"}, 0);
        booksTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(booksTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Button to add book
        JButton addBookButton = new JButton("Add Book");
        addBookButton.addActionListener(e -> new AddBookPage(this));
        buttonPanel.add(addBookButton);

        // Button to delete book
        JButton deleteBookButton = new JButton("Delete Book");
        deleteBookButton.addActionListener(e -> deleteSelectedBook());
        buttonPanel.add(deleteBookButton);

        // Button to logout
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        buttonPanel.add(logoutButton);

        add(panel);
        setVisible(true);

        // Fetch data from API and populate table
        fetchDataAndPopulateTable();
    }

    protected void fetchDataAndPopulateTable() {
        try {
            String response = NetworkUtil.doGet("/books");
            List<Book> books = JsonUtil.parseBooks(response); // Parse JSON using JsonUtil

            // Clear the existing table data
            tableModel.setRowCount(0);

            for (Book book : books) {
                tableModel.addRow(new Object[]{book.getId(), book.getTitle(), book.getAuthor()});
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch books data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bookId = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            NetworkUtil.doDelete("/delete-book/" + bookId);
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Book deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        try {
            NetworkUtil.doPost("/logout", "");
            JOptionPane.showMessageDialog(this, "Logged out successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginPage(); // Assuming you have a LoginPage class to navigate to
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to logout: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class JsonUtil {
        private static final Gson gson = new Gson();

        // Method to parse JSON array of books
        public static List<Book> parseBooks(String json) {
            java.lang.reflect.Type listType = new TypeToken<List<Book>>(){}.getType();
            return gson.fromJson(json, listType);
        }
    }
}
