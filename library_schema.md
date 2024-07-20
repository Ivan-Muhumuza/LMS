## `Library Management Schema`
    

__-- Book table: stores information about books__
```sql
CREATE TABLE Book (
    Isbn VARCHAR(225) PRIMARY KEY,
    Title VARCHAR(255),
    Author VARCHAR(255),
    IsAvailable BOOL,
    LibraryID INT,
    FOREIGN KEY (LibraryID) REFERENCES Library(LibraryID)
);
```

__-- Patron table: stores information about patrons__
```sql
CREATE TABLE Patron (
    PatronID INT PRIMARY KEY,
    Name VARCHAR(255),
    Password VARCHAR(255),
    Email VARCHAR(255) UNIQUE NOT NULL
);
```
__-- BorrowedBooks table: tracks borrowed books and their due dates__
```sql
CREATE TABLE BorrowedBooks (
    PatronID INT,
    Isbn VARCHAR(225),
    BorrowedDate DATETIME,
    DueDate DATETIME,
    PRIMARY KEY (PatronID, Isbn),
    FOREIGN KEY (PatronID) REFERENCES Patron(PatronID),
    FOREIGN KEY (Isbn) REFERENCES Book(Isbn)
);
```

__-- Transaction table: stores information about transactions__
```sql
CREATE TABLE Transaction (
    TransactionID INT PRIMARY KEY,
    PatronID INT,
    BookIsbn VARCHAR(225),
    TransactionDate DATETIME,
    DueDate DATETIME,
    TypeID INT,
    FOREIGN KEY (PatronID) REFERENCES Patron(PatronID),
    FOREIGN KEY (BookIsbn) REFERENCES Book(Isbn),
    FOREIGN KEY (TypeID) REFERENCES TransactionType(TypeID)
);
```

__-- Library table: stores information about libraries__
```sql
CREATE TABLE Library (
    LibraryID INT PRIMARY KEY,
    Name VARCHAR(255) NOT NULL
);
```

__-- Librarian table: stores information about librarians__
```sql
CREATE TABLE Librarian (
    LibrarianID INT PRIMARY KEY,
    LibraryID INT,
    Name VARCHAR(255),
    Email VARCHAR(255) UNIQUE NOT NULL,
    Password VARCHAR(255),
    FOREIGN KEY (LibraryID) REFERENCES Library(LibraryID)
);
```

__-- TransactionType table: stores types of transactions__
```sql
CREATE TABLE TransactionType (
    TypeID INT PRIMARY KEY AUTO_INCREMENT,
    TypeDescription VARCHAR(50) UNIQUE NOT NULL
);
```

__-- Insert initial data into TransactionType table__
```sql
INSERT INTO TransactionType (TypeDescription) VALUES
('borrow'),
('return'),
('renew');
```