import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.sql.SQLException;

public class BudgetifyApp extends Application {
    private TableView<Transaction> transactionTable;
    private TableView<Budget> budgetTable;
    private TableView<BankAccount> bankAccountTable;
    private ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    private ObservableList<Budget> budgets = FXCollections.observableArrayList();
    private ObservableList<BankAccount> bankAccounts = FXCollections.observableArrayList();
    private int currentUserId = 1; // For demo purposes

    // Model Classes
    public static class User {
        private final int userId;
        private final String email;
        private final String name;
        private final String password;

        public User(int userId, String email, String name, String password) {
            this.userId = userId;
            this.email = email;
            this.name = name;
            this.password = password;
        }

        // Getters
        public int getUserId() {
            return userId;
        }

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class Transaction {
        private final int transactionId;
        private final int userId;
        private final javafx.beans.property.SimpleStringProperty title;
        private final javafx.beans.property.SimpleDoubleProperty amount;
        private final javafx.beans.property.SimpleStringProperty category;
        private final javafx.beans.property.SimpleStringProperty date;
        private final javafx.beans.property.SimpleStringProperty paymentMethod;

        public Transaction(int transactionId, int userId, String title, double amount,
                String category, LocalDate date, String paymentMethod) {
            this.transactionId = transactionId;
            this.userId = userId;
            this.title = new javafx.beans.property.SimpleStringProperty(title);
            this.amount = new javafx.beans.property.SimpleDoubleProperty(amount);
            this.category = new javafx.beans.property.SimpleStringProperty(category);
            this.date = new javafx.beans.property.SimpleStringProperty(date.toString());
            this.paymentMethod = new javafx.beans.property.SimpleStringProperty(paymentMethod);
        }

        // Getters
        public int getTransactionId() {
            return transactionId;
        }

        public int getUserId() {
            return userId;
        }

        public String getTitle() {
            return title.get();
        }

        public double getAmount() {
            return amount.get();
        }

        public String getCategory() {
            return category.get();
        }

        public LocalDate getDate() {
            return LocalDate.parse(date.get());
        }

        public String getPaymentMethod() {
            return paymentMethod.get();
        }

        // Property getters for JavaFX binding
        public javafx.beans.property.StringProperty titleProperty() {
            return title;
        }

        public javafx.beans.property.DoubleProperty amountProperty() {
            return amount;
        }

        public javafx.beans.property.StringProperty categoryProperty() {
            return category;
        }

        public javafx.beans.property.StringProperty dateProperty() {
            return date;
        }

        public javafx.beans.property.StringProperty paymentMethodProperty() {
            return paymentMethod;
        }
    }

    public static class Budget {
        private final int budgetId;
        private final int userId;
        private final double monthlyBudget;
        private final String month;

        public Budget(int budgetId, int userId, double monthlyBudget, String month) {
            this.budgetId = budgetId;
            this.userId = userId;
            this.monthlyBudget = monthlyBudget;
            this.month = month;
        }

        // Getters
        public int getBudgetId() {
            return budgetId;
        }

        public int getUserId() {
            return userId;
        }

        public double getMonthlyBudget() {
            return monthlyBudget;
        }

        public String getMonth() {
            return month;
        }
    }

    public static class BankAccount {
        private final int accountId;
        private final int userId;
        private final String accountName;
        private final String firstName;
        private final String lastName;
        private final LocalDate dateOpened;
        private final int numberWithdrawals;
        private final int numberDeposits;
        private final String provider;

        public BankAccount(int accountId, int userId, String accountName, String firstName,
                String lastName, LocalDate dateOpened, int numberWithdrawals,
                int numberDeposits, String provider) {
            this.accountId = accountId;
            this.userId = userId;
            this.accountName = accountName;
            this.firstName = firstName;
            this.lastName = lastName;
            this.dateOpened = dateOpened;
            this.numberWithdrawals = numberWithdrawals;
            this.numberDeposits = numberDeposits;
            this.provider = provider;
        }

        // Getters
        public int getAccountId() {
            return accountId;
        }

        public int getUserId() {
            return userId;
        }

        public String getAccountName() {
            return accountName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public LocalDate getDateOpened() {
            return dateOpened;
        }

        public int getNumberWithdrawals() {
            return numberWithdrawals;
        }

        public int getNumberDeposits() {
            return numberDeposits;
        }

        public String getProvider() {
            return provider;
        }
    }

    // CRUD Operations for Users
    private void createUser(String email, String name, String password) {
        try {
            DatabaseManager.getInstance().createUser(email, name, password);
            showAlert("Success", "User created successfully");
        } catch (SQLException e) {
            showAlert("Error", "Failed to create user: " + e.getMessage());
        }
    }

    private User readUser(int userId) {
        try {
            return DatabaseManager.getInstance().getUserByEmail(email);
        } catch (SQLException e) {
            showAlert("Error", "Failed to read user: " + e.getMessage());
            return null;
        }
    }

    private void updateUser(int userId, String email, String name, String password) {
        try {
            DatabaseManager.getInstance().updateUser(userId, email, name, password);
            showAlert("Success", "User updated successfully");
        } catch (SQLException e) {
            showAlert("Error", "Failed to update user: " + e.getMessage());
        }
    }

    private void deleteUser(int userId) {
        try {
            DatabaseManager.getInstance().deleteUser(userId);
            showAlert("Success", "User deleted successfully");
        } catch (SQLException e) {
            showAlert("Error", "Failed to delete user: " + e.getMessage());
        }
    }

    // CRUD Operations for Transactions
    private void createTransaction(String title, double amount, String category,
            LocalDate date, String paymentMethod) {
        try {
            DatabaseManager.getInstance().createTransaction(currentUserId, title, amount,
                    category, date, paymentMethod);
            refreshTransactions();
            showAlert("Success", "Transaction created successfully");
        } catch (SQLException e) {
            showAlert("Error", "Failed to create transaction: " + e.getMessage());
        }
    }

    private Transaction readTransaction(int transactionId) {
        try {
            return DatabaseManager.getInstance().getTransaction(transactionId);
        } catch (SQLException e) {
            showAlert("Error", "Failed to read transaction: " + e.getMessage());
            return null;
        }
    }

    private void updateTransaction(int transactionId, String title, double amount,
            String category, LocalDate date, String paymentMethod) {
        try {
            DatabaseManager.getInstance().updateTransaction(transactionId, title, amount,
                    category, date, paymentMethod);
            refreshTransactions();
            showAlert("Success", "Transaction updated successfully");
        } catch (SQLException e) {
            showAlert("Error", "Failed to update transaction: " + e.getMessage());
        }
    }

    private void deleteTransaction(int transactionId) {
        try {
            DatabaseManager.getInstance().deleteTransaction(transactionId);
            refreshTransactions();
            showAlert("Success", "Transaction deleted successfully");
        } catch (SQLException e) {
            showAlert("Error", "Failed to delete transaction: " + e.getMessage());
        }
    }

    // CRUD Operations for Budgets
    private void createBudget(double monthlyBudget, String month) {
        try {
            DatabaseManager.getInstance().createBudget(currentUserId, monthlyBudget, month);
            refreshBudgets();
            showAlert("Success", "Budget created successfully");
        } catch (SQLException e) {
            showAlert("Error", "Failed to create budget: " + e.getMessage());
        }
    }

    private Budget readBudget(int budgetId) {
        try {
            return DatabaseManager.getInstance().getBudget(budgetId);
        } catch (SQLException e) {
            showAlert("Error", "Failed to read budget: " + e.getMessage());
            return null;
        }
    }

    private void updateBudget(int budgetId, double monthlyBudget, String month) {
        try {
            DatabaseManager.getInstance().updateBudget(budgetId, monthlyBudget, month);
            refreshBudgets();
            showAlert("Success", "Budget updated successfully");
        } catch (SQLException e) {
            showAlert("Error", "Failed to update budget: " + e.getMessage());
        }
    }

    private void deleteBudget(int budgetId) {
        try {
            DatabaseManager.getInstance().deleteBudget(budgetId);
            refreshBudgets();
            showAlert("Success", "Budget deleted successfully");
        } catch (SQLException e) {
            showAlert("Error", "Failed to delete budget: " + e.getMessage());
        }
    }

    // CRUD Operations for Bank Accounts
    private void createBankAccount(String accountName, String firstName, String lastName,
            LocalDate dateOpened, String provider) {
        try {
            DatabaseManager.getInstance().createBankAccount(currentUserId, accountName,
                    firstName, lastName, dateOpened, provider);
            refreshBankAccounts();
            showAlert("Success", "Bank account created successfully");
        } catch (SQLException e) {
            showAlert("Error", "Failed to create bank account: " + e.getMessage());
        }
    }

    private BankAccount readBankAccount(int accountId) {
        try {
            return DatabaseManager.getInstance().getBankAccount(accountId);
        } catch (SQLException e) {
            showAlert("Error", "Failed to read bank account: " + e.getMessage());
            return null;
        }
    }

    private void updateBankAccount(int accountId, String accountName, String firstName,
            String lastName, String provider) {
        try {
            DatabaseManager.getInstance().updateBankAccount(accountId, accountName,
                    firstName, lastName, provider);
            refreshBankAccounts();
            showAlert("Success", "Bank account updated successfully");
        } catch (SQLException e) {
            showAlert("Error", "Failed to update bank account: " + e.getMessage());
        }
    }

    private void deleteBankAccount(int accountId) {
        try {
            DatabaseManager.getInstance().deleteBankAccount(accountId);
            refreshBankAccounts();
            showAlert("Success", "Bank account deleted successfully");
        } catch (SQLException e) {
            showAlert("Error", "Failed to delete bank account: " + e.getMessage());
        }
    }

    // Refresh methods for tables
    private void refreshTransactions() {
        try {
            transactions.clear();
            transactions.addAll(DatabaseManager.getInstance().getTransactions(currentUserId));
        } catch (SQLException e) {
            showAlert("Error", "Failed to refresh transactions: " + e.getMessage());
        }
    }

    private void refreshBudgets() {
        try {
            budgets.clear();
            budgets.addAll(DatabaseManager.getInstance().getBudgets(currentUserId));
        } catch (SQLException e) {
            showAlert("Error", "Failed to refresh budgets: " + e.getMessage());
        }
    }

    private void refreshBankAccounts() {
        try {
            bankAccounts.clear();
            bankAccounts.addAll(DatabaseManager.getInstance().getBankAccounts(currentUserId));
        } catch (SQLException e) {
            showAlert("Error", "Failed to refresh bank accounts: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Budgetify");

        // Create main layout
        BorderPane mainLayout = new BorderPane();

        // Create login form
        VBox loginForm = createLoginForm();
        mainLayout.setCenter(loginForm);

        // Create scene
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createLoginForm() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setMaxWidth(300);

        Label titleLabel = new Label("Budgetify Login");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        loginButton.setOnAction(e -> handleLogin(emailField.getText(), passwordField.getText()));
        registerButton.setOnAction(e -> showRegisterForm());

        form.getChildren().addAll(titleLabel, emailField, passwordField, loginButton, registerButton);
        return form;
    }

    private void handleLogin(String email, String password) {
        String userInfo = helper_functions.getUserByEmail(email);
        if (userInfo != null && userInfo.contains(password)) {
            showDashboard();
        } else {
            showAlert("Login Failed", "Invalid email or password");
        }
    }

    private void showDashboard() {
        BorderPane dashboard = new BorderPane();

        // Create tab pane for different sections
        TabPane tabPane = new TabPane();

        // Transactions Tab
        Tab transactionsTab = new Tab("Transactions");
        VBox transactionsContent = new VBox(10);
        transactionsContent.setPadding(new Insets(20));
        transactionsContent.getChildren().addAll(
                createTransactionForm(),
                createTransactionTable());
        transactionsTab.setContent(transactionsContent);

        // Budgets Tab
        Tab budgetsTab = new Tab("Budgets");
        VBox budgetsContent = new VBox(10);
        budgetsContent.setPadding(new Insets(20));
        budgetsContent.getChildren().addAll(
                createBudgetForm(),
                createBudgetTable());
        budgetsTab.setContent(budgetsContent);

        // Bank Accounts Tab
        Tab bankAccountsTab = new Tab("Bank Accounts");
        VBox bankAccountsContent = new VBox(10);
        bankAccountsContent.setPadding(new Insets(20));
        bankAccountsContent.getChildren().addAll(
                createBankAccountForm(),
                createBankAccountTable());
        bankAccountsTab.setContent(bankAccountsContent);

        tabPane.getTabs().addAll(transactionsTab, budgetsTab, bankAccountsTab);
        dashboard.setCenter(tabPane);

        // Refresh all data
        refreshTransactions();
        refreshBudgets();
        refreshBankAccounts();

        // Set the new scene
        Scene scene = new Scene(dashboard, 1000, 800);
        Stage stage = (Stage) transactionTable.getScene().getWindow();
        stage.setScene(scene);
    }

    private VBox createTransactionForm() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");
        DatePicker datePicker = new DatePicker();
        TextField paymentMethodField = new TextField();
        paymentMethodField.setPromptText("Payment Method");

        Button addButton = new Button("Add Transaction");
        addButton.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                helper_functions.createTransaction(
                        currentUserId,
                        titleField.getText(),
                        amount,
                        categoryField.getText(),
                        datePicker.getValue().toString(),
                        paymentMethodField.getText());
                refreshTransactions();
                clearForm(titleField, amountField, categoryField, datePicker, paymentMethodField);
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid amount");
            }
        });

        form.getChildren().addAll(
                new Label("Add New Transaction"),
                titleField,
                amountField,
                categoryField,
                datePicker,
                paymentMethodField,
                addButton);

        return form;
    }

    private TableView<Transaction> createTransactionTable() {
        TableView<Transaction> table = new TableView<>();

        TableColumn<Transaction, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(cellData -> cellData.getValue().titleProperty());

        TableColumn<Transaction, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());

        TableColumn<Transaction, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());

        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        TableColumn<Transaction, String> methodCol = new TableColumn<>("Payment Method");
        methodCol.setCellValueFactory(cellData -> cellData.getValue().paymentMethodProperty());

        table.getColumns().addAll(titleCol, amountCol, categoryCol, dateCol, methodCol);
        table.setItems(transactions);

        return table;
    }

    private TableView<Budget> createBudgetTable() {
        TableView<Budget> table = new TableView<>();

        TableColumn<Budget, Double> monthlyBudgetCol = new TableColumn<>("Monthly Budget");
        monthlyBudgetCol.setCellValueFactory(
                cellData -> new SimpleDoubleProperty(cellData.getValue().getMonthlyBudget()).asObject());

        TableColumn<Budget, String> monthCol = new TableColumn<>("Month");
        monthCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMonth()));

        table.getColumns().addAll(monthlyBudgetCol, monthCol);
        table.setItems(budgets);

        return table;
    }

    private TableView<BankAccount> createBankAccountTable() {
        TableView<BankAccount> table = new TableView<>();

        TableColumn<BankAccount, String> accountNameCol = new TableColumn<>("Account Name");
        accountNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAccountName()));

        TableColumn<BankAccount, String> nameCol = new TableColumn<>("Account Holder");
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName() + " " +
                cellData.getValue().getLastName()));

        TableColumn<BankAccount, String> dateOpenedCol = new TableColumn<>("Date Opened");
        dateOpenedCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getDateOpened().toString()));

        TableColumn<BankAccount, String> providerCol = new TableColumn<>("Provider");
        providerCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProvider()));

        TableColumn<BankAccount, Integer> withdrawalsCol = new TableColumn<>("Withdrawals");
        withdrawalsCol.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getNumberWithdrawals()).asObject());

        TableColumn<BankAccount, Integer> depositsCol = new TableColumn<>("Deposits");
        depositsCol.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getNumberDeposits()).asObject());

        table.getColumns().addAll(accountNameCol, nameCol, dateOpenedCol, providerCol,
                withdrawalsCol, depositsCol);
        table.setItems(bankAccounts);

        return table;
    }

    private void showRegisterForm() {
        // TODO: Implement register form
        showAlert("Register", "Register functionality not implemented yet");
    }

    private void clearForm(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void clearForm(TextField titleField, TextField amountField, TextField categoryField,
            DatePicker datePicker, TextField paymentMethodField) {
        titleField.clear();
        amountField.clear();
        categoryField.clear();
        datePicker.setValue(null);
        paymentMethodField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private VBox createBudgetForm() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));

        Label titleLabel = new Label("Add New Budget");
        TextField monthlyBudgetField = new TextField();
        monthlyBudgetField.setPromptText("Monthly Budget Amount");
        TextField monthField = new TextField();
        monthField.setPromptText("Month (e.g., April 2025)");
        Button addButton = new Button("Add Budget");

        addButton.setOnAction(e -> {
            try {
                double monthlyBudget = Double.parseDouble(monthlyBudgetField.getText());
                String month = monthField.getText();
                createBudget(monthlyBudget, month);
                clearForm(monthlyBudgetField, monthField);
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid budget amount");
            }
        });

        form.getChildren().addAll(titleLabel, monthlyBudgetField, monthField, addButton);
        return form;
    }

    private VBox createBankAccountForm() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));

        Label titleLabel = new Label("Add New Bank Account");
        TextField accountNameField = new TextField();
        accountNameField.setPromptText("Account Name");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        DatePicker dateOpenedPicker = new DatePicker();
        dateOpenedPicker.setPromptText("Date Opened");
        TextField providerField = new TextField();
        providerField.setPromptText("Provider");
        Button addButton = new Button("Add Bank Account");

        addButton.setOnAction(e -> {
            if (dateOpenedPicker.getValue() == null) {
                showAlert("Error", "Please select a date");
                return;
            }
            createBankAccount(
                    accountNameField.getText(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    dateOpenedPicker.getValue(),
                    providerField.getText());
            clearForm(accountNameField, firstNameField, lastNameField, providerField);
            dateOpenedPicker.setValue(null);
        });

        form.getChildren().addAll(
                titleLabel, accountNameField, firstNameField, lastNameField,
                dateOpenedPicker, providerField, addButton);
        return form;
    }

    public static void main(String[] args) {
        launch(args);
    }
}