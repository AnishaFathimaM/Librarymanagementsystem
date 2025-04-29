import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

class Book {
    int id;
    String name, author, genre;
    String studentName, studentId;
    boolean isIssued;
    String issueDate, dueDate, returnDate;

    public Book(int id, String name, String author, String genre) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.isIssued = false;
    }

    public void issueBook(String studentName, String studentId) {
        if (!isIssued) {
            this.isIssued = true;
            this.studentName = studentName;
            this.studentId = studentId;
            Date today = new Date();
            this.issueDate = new SimpleDateFormat("dd-MM-yyyy").format(today);
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            cal.add(Calendar.DAY_OF_MONTH, 14);
            this.dueDate = new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
            System.out.println("Book issued successfully to " + studentName);
        } else {
            System.out.println("Book is already issued.");
        }
    }

    public void returnBook() {
        if (isIssued) {
            Date today = new Date();
            this.returnDate = new SimpleDateFormat("dd-MM-yyyy").format(today);
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date due = sdf.parse(dueDate);
                long delay = (today.getTime() - due.getTime()) / (1000 * 60 * 60 * 24);
                if (delay > 0) {
                    System.out.println("Returned late by " + delay + " days. Fine: ₹" + (delay * 5));
                } else {
                    System.out.println("Returned on time.");
                }
            } catch (Exception e) {
                System.out.println("Error in date parsing.");
            }
            this.isIssued = false;
            this.studentName = null;
            this.studentId = null;
            this.issueDate = null;
            this.dueDate = null;
        } else {
            System.out.println("Book is not issued.");
        }
    }

    public void display() {
        System.out.println("ID: " + id + " | Book: " + name + " | Author: " + author + " | Genre: " + genre);
        System.out.println("Issued: " + isIssued);
        if (isIssued) {
            System.out.println("Issued to: " + studentName + " | Due: " + dueDate);
        }
    }

    public String toCsv() {
        return String.join(",", String.valueOf(id), name, author, genre, String.valueOf(isIssued),
                studentName != null ? studentName : "",
                studentId != null ? studentId : "",
                issueDate != null ? issueDate : "",
                dueDate != null ? dueDate : "",
                returnDate != null ? returnDate : "");
    }
}

class User {
    String username, password;
    String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public boolean validate(String u, String p) {
        return this.username.equals(u) && this.password.equals(p);
    }

    public String toCsv() {
        return String.join(",", username, password, role);
    }
}

public class LibrarySystem {
    static ArrayList<Book> books = new ArrayList<>();
    static ArrayList<User> users = new ArrayList<>();
    static final String BOOK_FILE = "books.csv";
    static final String USER_FILE = "users.csv";
    static final String BOOK_ID_FILE = "book_id_counter.txt";
    static int nextBookId = 1;

    public static void main(String[] args) {
        loadBooks();
        loadUsers();
        Scanner sc = new Scanner(System.in);

        System.out.println("Choose any option below\n1. Admin Login\n2. Student Login\n3. Student Register");
        int option = sc.nextInt();
        sc.nextLine();

        switch (option) {
            case 1 -> adminLogin(sc);
            case 2 -> studentLogin(sc);
            case 3 -> registerStudent(sc);
            default -> System.out.println("Invalid option.");
        }

        saveBooks();
        saveUsers();
        saveBookId();
    }

    static void adminLogin(Scanner sc) {
        System.out.println("Admin Login:");
        System.out.print("Username: ");
        String u = sc.nextLine();
        System.out.print("Password: ");
        String p = sc.nextLine();

        if (u.equals("admin") && p.equals("admin123")) {
            System.out.println("Welcome Admin!");
            adminMenu(sc);
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    static void adminMenu(Scanner sc) {
        while (true) {
            System.out.println("\nAdmin Menu:\n1. Add Book\n2. Update Book\n3. Delete Book\n4. View Borrowed\n5. View Overdue\n6. View Users\n7. Exit");
            int ch = sc.nextInt(); sc.nextLine();
            switch (ch) {
                case 1 -> addBook(sc);
                case 2 -> updateBook(sc);
                case 3 -> deleteBook(sc);
                case 4 -> viewBorrowedBooks();
                case 5 -> viewOverdueBooks();
                case 6 -> viewUsers();
                case 7 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    static void addBook(Scanner sc) {
        System.out.print("Book Name: "); String name = sc.nextLine();
        System.out.print("Author: "); String author = sc.nextLine();
        System.out.print("Genre: "); String genre = sc.nextLine();
        books.add(new Book(nextBookId++, name, author, genre));
        System.out.println("Book added.");
    }

    static void updateBook(Scanner sc) {
        System.out.print("Book ID to Update: "); int id = sc.nextInt(); sc.nextLine();
        for (Book b : books) {
            if (b.id == id) {
                System.out.print("New Author: "); b.author = sc.nextLine();
                System.out.print("New Genre: "); b.genre = sc.nextLine();
                System.out.println("Book updated.");
                return;
            }
        }
        System.out.println("Book not found.");
    }

    static void deleteBook(Scanner sc) {
        System.out.print("Book ID to Delete: "); int id = sc.nextInt(); sc.nextLine();
        books.removeIf(b -> b.id == id);
        System.out.println("Book deleted (if it existed).");
    }

    static void viewBorrowedBooks() {
        for (Book b : books) {
            if (b.isIssued) b.display();
        }
    }

    static void viewOverdueBooks() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date today = new Date();
            for (Book b : books) {
                if (b.isIssued && sdf.parse(b.dueDate).before(today)) {
                    System.out.println("Overdue:");
                    b.display();
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing date.");
        }
    }

    static void viewUsers() {
        for (User u : users) {
            if (u.role.equals("student")) {
                System.out.println("Username: " + u.username);
                for (Book b : books) {
                    if (u.username.equals(b.studentId)) {
                        b.display();
                    }
                }
            }
        }
    }

    static void studentLogin(Scanner sc) {
        System.out.println("Student Login:");
        System.out.print("Username: ");
        String u = sc.nextLine();
        System.out.print("Password: ");
        String p = sc.nextLine();

        for (User user : users) {
            if (user.role.equals("student") && user.validate(u, p)) {
                System.out.println("Welcome " + u);
                studentMenu(sc, user);
                return;
            }
        }
        System.out.println("Invalid student credentials.");
    }

    static void studentMenu(Scanner sc, User user) {
        while (true) {
            System.out.println("\nStudent Menu:\n1. Borrow Book\n2. Return Book\n3. Search Book\n4. View History\n5. Edit Profile\n6. Exit");
            int ch = sc.nextInt(); sc.nextLine();
            switch (ch) {
                case 1 -> {
                    long count = books.stream().filter(b -> b.isIssued && b.studentId.equals(user.username)).count();
                    if (count >= 3) {
                        System.out.println("You have already borrowed 3 books.");
                        break;
                    }
                    System.out.print("Book Name: ");
                    String book = sc.nextLine();
                    for (Book b : books) {
                        if (b.name.equalsIgnoreCase(book) && !b.isIssued) {
                            b.issueBook(user.username, user.username);
                            return;
                        }
                    }
                    System.out.println("Book not found or already issued.");
                }
                case 2 -> {
                    System.out.print("Book Name: ");
                    String book = sc.nextLine();
                    for (Book b : books) {
                        if (b.name.equalsIgnoreCase(book) && b.isIssued && b.studentId.equals(user.username)) {
                            b.returnBook();
                            return;
                        }
                    }
                    System.out.println("Book not found or not issued to you.");
                }
                case 3 -> {
                    System.out.print("Search Book Name: ");
                    String search = sc.nextLine();
                    for (Book b : books) {
                        if (b.name.toLowerCase().contains(search.toLowerCase())) {
                            b.display();
                        }
                    }
                }
                case 4 -> {
                    for (Book b : books) {
                        if ((b.studentId != null && b.studentId.equals(user.username)) ||
                            (b.returnDate != null && b.studentId.equals(user.username))) {
                            b.display();
                        }
                    }
                }
                case 5 -> {
                    System.out.print("Enter new username: ");
                    String newU = sc.nextLine();
                    System.out.print("Enter new password: ");
                    String newP = sc.nextLine();
                    for (Book b : books) {
                        if (b.studentId != null && b.studentId.equals(user.username)) {
                            b.studentId = newU;
                            b.studentName = newU;
                        }
                    }
                    user.username = newU;
                    user.password = newP;
                    System.out.println("Profile updated.");
                }
                case 6 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void registerStudent(Scanner sc) {
        System.out.print("Enter new username: ");
        String u = sc.nextLine();
        System.out.print("Enter password: ");
        String p = sc.nextLine();
        users.add(new User(u, p, "student"));
        System.out.println("Student registered successfully.");
    }

    static void loadBooks() {
        try (BufferedReader br = new BufferedReader(new FileReader(BOOK_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] t = line.split(",", -1);
                Book b = new Book(Integer.parseInt(t[0]), t[1], t[2], t[3]);
                b.isIssued = Boolean.parseBoolean(t[4]);
                b.studentName = t[5];
                b.studentId = t[6];
                b.issueDate = t[7];
                b.dueDate = t[8];
                b.returnDate = t[9];
                books.add(b);
            }
        } catch (IOException ignored) {}

        loadBookId();
    }

    static void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] t = line.split(",", -1);
                if (t[2].equals("student")) {
                    users.add(new User(t[0], t[1], t[2]));
                }
            }
        } catch (IOException ignored) {}
    }

    static void saveBooks() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOK_FILE))) {
            for (Book b : books) {
                bw.write(b.toCsv());
                bw.newLine();
            }
        } catch (IOException ignored) {}
    }

    static void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User u : users) {
                bw.write(u.toCsv());
                bw.newLine();
            }
        } catch (IOException ignored) {}
    }

    static void loadBookId() {
        try (BufferedReader br = new BufferedReader(new FileReader(BOOK_ID_FILE))) {
            nextBookId = Integer.parseInt(br.readLine());
        } catch (IOException ignored) {}
    }

    static void saveBookId() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOK_ID_FILE))) {
            bw.write(String.valueOf(nextBookId));
        } catch (IOException ignored) {}
    }
}
