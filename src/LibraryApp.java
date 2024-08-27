
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryApp extends JFrame {
    private JTable bookTable;
    private JTextField titleField, authorField, isbnField, genreField, yearField;
    private List<Book> books;
    private File file;
    
    private Color setColor() {
    	return new Color(255,255,255);
    }

    public LibraryApp() {
        books = new ArrayList<>();
        file = new File("library.txt");

        // Create header label
        JLabel headerLabel = new JLabel("Simple Library System", JLabel.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 30)); // Set font and size

        // Define the columns for the table
        String[] columnNames = {"Title", "Author", "ISBN", "Genre", "Year"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        bookTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(bookTable);

        // Define input fields for book attributes
        titleField = new JTextField("");
        authorField = new JTextField("");
        isbnField = new JTextField("");
        genreField = new JTextField("");
        yearField = new JTextField("");

        // Define buttons for CRUD operations
        JButton addButton = new JButton("Add Book");
        addButton.setForeground(setColor());
        addButton.setBackground(new Color(33, 77, 252));
        JButton updateButton = new JButton("Update Book");
        
        updateButton.setForeground(setColor());
        updateButton.setBackground(new Color(30, 201, 79));
        JButton deleteButton = new JButton("Delete Book");
        deleteButton.setForeground(setColor());
        deleteButton.setBackground(new Color(235, 38, 38));
        JButton loadButton = new JButton("Load Library");
        loadButton.setForeground(setColor());
        loadButton.setBackground(new Color(38, 215, 235));

        // Add book button action
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        // Update book button action
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateBook();
            }
        });

        // Delete book button action
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
        });

        // Save library button action
//        saveButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                saveLibrary();
//            }
//        });

        // Load library button action
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadLibrary();
            }
        });

        // Handle row selection in the table
        bookTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow != -1) {
                    titleField.setText(bookTable.getValueAt(selectedRow, 0).toString());
                    authorField.setText(bookTable.getValueAt(selectedRow, 1).toString());
                    isbnField.setText(bookTable.getValueAt(selectedRow, 2).toString());
                    genreField.setText(bookTable.getValueAt(selectedRow, 3).toString());
                    yearField.setText(bookTable.getValueAt(selectedRow, 4).toString());
                }
            }
        });

        // Panel for book attributes input
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author:"));
        inputPanel.add(authorField);
        inputPanel.add(new JLabel("ISBN:"));
        inputPanel.add(isbnField);
        inputPanel.add(new JLabel("Genre:"));
        inputPanel.add(genreField);
        inputPanel.add(new JLabel("Year:"));
        inputPanel.add(yearField);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
       
        buttonPanel.add(loadButton);

        // Main panel to hold all other panels
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(headerLabel, BorderLayout.NORTH); // Add the header at the top
        mainPanel.add(inputPanel, BorderLayout.CENTER); // Center input fields and buttons
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(scrollPane, BorderLayout.EAST);

        // Frame settings
        setTitle("LibraryApp");
        setSize(750,300);
        add(mainPanel);
        //pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Book class to hold book data
    class Book {
        String title, author, isbn, genre;
        int year;

        Book(String title, String author, String isbn, String genre, int year) {
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.genre = genre;
            this.year = year;
        }

        @Override
        public String toString() {
            return title + "," + author + "," + isbn + "," + genre + "," + year;
        }
    }
    
    private void addBook() {
        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();
        String genre = genreField.getText();
        int year = Integer.parseInt(yearField.getText());

        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || genre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.");
            return;
        }

        Book book = new Book(title, author, isbn, genre, year);
        books.add(book);
        
        if(!isDuplicate(book)) {
        	appendToFile(book);
            updateTable();
            clearFields();
        }else {
        	 JOptionPane.showMessageDialog(this, "This book has already in system! ");
        	 
        }
        
        
        
    }

    private void updateBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a book to update.");
            return;
        }

        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();
        String genre = genreField.getText();
        int year = Integer.parseInt(yearField.getText());

        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || genre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.");
            return;
        }

        Book updatedBook = new Book(title, author, isbn, genre, year);
        books.set(selectedRow, updatedBook);
        overwriteFile();
        updateTable();
        clearFields();
    }

    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a book to delete.");
            return;
        }

        books.remove(selectedRow);
        overwriteFile();
        updateTable();
        clearFields();
    }

//    private void saveLibrary() {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
//            for (Book book : books) {
//                writer.write(book.toString());
//                writer.newLine();
//            }
//            JOptionPane.showMessageDialog(this, "Library saved successfully.");
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage());
//        }
//    }

    private void loadLibrary() {
        books.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String title = parts[0];
                String author = parts[1];
                String isbn = parts[2];
                String genre = parts[3];
                int year = Integer.parseInt(parts[4]);
                books.add(new Book(title, author, isbn, genre, year));
            }
            updateTable();
           
            
            if(books.isEmpty()) {
            	 JOptionPane.showMessageDialog(this, "There is no book in library system!");
            }else {
            	 JOptionPane.showMessageDialog(this, "Library loaded successfully.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage());
        }
    }

    private void appendToFile(Book book) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(book.toString());
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage());
        }
    }

    private void overwriteFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (Book book : books) {
                writer.write(book.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage());
        }
    }
    
    private boolean isDuplicate(Book book) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String existingTitle = parts[0];
                String existingAuthor = parts[1];
                String existingIsbn = parts[2];

                if (book.title.equals(existingTitle) && book.author.equals(existingAuthor) && book.isbn.equals(existingIsbn)) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage());
        }
        return false;
    }
    
    private void updateTable() {
        DefaultTableModel model = (DefaultTableModel) bookTable.getModel();
        model.setRowCount(0);
        for (Book book : books) {
            model.addRow(new Object[]{book.title, book.author, book.isbn, book.genre, book.year});
        }
    }

    private void clearFields() {
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
        genreField.setText("");
        yearField.setText("");
    }


    // Other methods (addBook, updateBook, deleteBook, saveLibrary, loadLibrary, etc.) remain the same...
    
    public static void main(String[] args) {
        new LibraryApp();
    }
}
