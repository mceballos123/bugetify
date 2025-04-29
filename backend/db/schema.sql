CREATE TABLE Users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Create Transactions table
CREATE TABLE Transactions (
    TransactionID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL, 
    Title VARCHAR(255) NOT NULL,
    Amount DECIMAL(10, 2) NOT NULL,
    TransactionCategory VARCHAR(100) NOT NULL,
    TransactionDate DATE NOT NULL,
    PaymentMethod VARCHAR(100) NOT NULL,
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Create Budget table
CREATE TABLE Budget (
    BudgetID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    MonthlyBudget DECIMAL(10, 2) NOT NULL,
    Month VARCHAR(50) NOT NULL,
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Create Bank Accounts table
CREATE TABLE BankAccounts (
    AccountID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    Account_name VARCHAR(255) NOT NULL,
    First_name VARCHAR(100) NOT NULL,
    Last_name VARCHAR(100) NOT NULL,
    Date_opened DATE NOT NULL,
    Number_withdrawals INT DEFAULT 0,
    Number_deposits INT DEFAULT 0,
    Provider VARCHAR(100) NOT NULL,
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);