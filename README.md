# Librarymanagementsystem
The Library Management System is a Java-based application designed to manage both book records and student activities, providing a seamless experience for both administrators and students. It features basic functionalities, including user authentication, book borrowing and returning, overdue fine calculation, and a simple GUI-less interface. Data is persistently stored in CSV files, ensuring that the system retains its state between different runs.

# Admin Side
The system allows administrators to log in with fixed credentials and perform a range of management tasks. The primary operations available to admins are book management (add, update, delete), viewing borrowed books, and handling overdue items.

To handle authentication, the admin uses the adminLogin() method, where credentials are validated before granting access to the admin menu. In this menu, admins can add books using the addBook() method, update existing records using updateBook(), and delete books with the deleteBook() method. The system also provides the ability to view borrowed books (viewBorrowedBooks()) and overdue books (viewOverdueBooks()). To maintain efficient management of registered users, the viewUsers() method is implemented, allowing admins to see the details of all students and their associated borrowed books.

# Student Side
For students, the system supports user registration (registerStudent()), login (studentLogin()), borrowing books, returning books, searching available books, and managing profiles.

When a student registers, they use the registerStudent() method, which prompts them to create a username and password, storing them in a CSV file for future login attempts. Once registered, students can log in using the studentLogin() method, where their credentials are verified. After logging in, students are presented with a menu that allows them to borrow books, return books, search the library catalog, view their borrowing history, and edit their profile.

The borrowing and returning process is handled through the issueBook() and returnBook() methods, respectively, which are part of the Book class. These methods not only track whether a book is issued, but also calculate overdue fines if the return date exceeds the due date. The return process is connected to fine calculation, where the number of overdue days is determined and a fine of ₹5 per day is applied.

For searching books, the system implements a simple search functionality where students can input keywords, and the system checks the book titles for matches. The search results are displayed along with book details.

The system shows how basic file handling, object-oriented design, and Java's date features can be used to solve real-life problems.


