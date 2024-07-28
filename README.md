# LMS

[Loom video link here](https://www.loom.com/share/776d81e8f0e143a7b54591635ad65af1?sid=93bb4a34-5e54-47f9-814e-dcc64c99052c)

[LMS-Testing Loom video](https://www.loom.com/share/a80da2e1fd5d494faa30d1a64fc95147?sid=2d203d73-c33a-4d1e-b1a2-5c33f94cfd4e)

# Library Management System

## Overview

This Library Management System (LMS) is designed to provide a comprehensive solution for managing library resources and operations. It incorporates various software development principles and technologies such as object-oriented programming (OOP), data structures, database design, SQL, JDBC, and JavaFX for the user interface. The project is structured to ensure scalability, flexibility, and ease of maintenance.

## Project Structure

- `app`: Contains the main application entry point (`LMSApplication.java`).
- `controller`: Manages the logic and interactions between the UI and the underlying services.
- `model`: Holds the entity classes (`Book`, `BorrowedBook`, `Librarian`, `Patron`, `Transaction`, `TransactionType`).
- `repository`: Handles database interactions.
- `service`: Contains the business logic.
- `util`: Includes utility classes such as `DatabaseUtil` for database configuration.
- `test`: Contains integration tests for the repository and unit tests for the services.

## Setup Instructions

### Prerequisites

- Java 21
- MySQL or H2 Database (for testing)
- Maven (for managing dependencies and building the project)
- IDE (e.g., IntelliJ IDEA, Eclipse)
- JavaFX SDK

### Step-by-Step Guide

1. **Clone the Repository**

   ```bash
   git clone https://github.com/Ivan-Muhumuza/LMS.git
   cd LMS
   ```

2. **Set Up the Database**

    - **MySQL**:
        - Create a database for the library system.
        - Update the `DatabaseUtil.java` file in the `util` package with your MySQL database credentials.

    - **H2 Database**:
        - No setup is required. H2 will be used for integration testing.

3. **Build the Project**

   Ensure you have Maven installed, then run the following command to build the project and download dependencies:

   ```bash
   mvn clean install
   ```

4. **Run the Application**

   Use your IDE to run the `LMSApplication.java` file located in the `app` package.

5. **Running Tests**

   To run the unit and integration tests using JUnit 5 and Mockito, execute the following command:

   ```bash
   mvn test
   ```

   Code coverage is managed by Jacoco and will generate a coverage report after the tests are run. To view the report, open the `target/site/jacoco/index.html` file in a web browser.

## Usage

### User Interface

The application uses JavaFX to provide a user-friendly interface. The main features include:

- **Login**: Authenticate as a librarian.
- **Dashboard**: Access various management functionalities such as adding books, managing patrons, and handling transactions.
- **Borrow/Return Books**: Manage the borrowing and returning of books.
- **View Records**: Display lists of books, patrons, and transaction histories.

### Core Functionalities

- **Books**: Add, update, delete, and search for books.
- **Patrons**: Manage patron records including adding, updating, and deleting patrons.
- **Transactions**: Handle borrowing and returning of books with proper transaction records.
- **Authentication**: Secure login for librarians.

### Key Classes and Methods

- **`LMSApplication.java`**: Entry point of the application.
- **Controllers**:
    - `LoginController`: Manages user authentication and navigation to the dashboard.
    - `LibrarianDashboardController`: Provides functionalities for managing books, patrons, and transactions.
- **Models**:
    - `Book`, `BorrowedBook`, `Librarian`, `Patron`, `Transaction`, `TransactionType`: Define the main entities used in the system.
- **Repositories**:
    - `BookRepository`, `BorrowedBookRepository`, `PatronRepository`, `TransactionRepository`: Handle database interactions for the respective entities.
- **Services**:
    - `BookService`, `BorrowedBookService`, `PatronService`, `TransactionService`: Implement business logic.
- **Utilities**:
    - `DatabaseUtil`: Manages database connections and configurations.
