public class Main {
    public static void main(String[] args) {
        // Just for test purposes
        UserDAO.addUser("mariam@example.com", "Mariam", "password123");

        // Add a sample transaction
        TransactionDAO.addTransaction(
                1,                          // userId (update this based on your DB!)
                "Coffee",
                4.75,
                "Food & Drink",
                "2025-05-07",
                "Credit Card"
        );

        TransactionDAO.updateTransaction(
                1,
                "Updated Coffee",
                5.50,
                "Food",
                "2025-05-07",
                "Debit Card"
        );

// DELETE it (just for testing cleanup)
        TransactionDAO.deleteTransaction(1);

        // Fetch and print all transactions for user 1
        for (String t : TransactionDAO.getTransactionsByUserId(1)) {
            System.out.println(t);
        }

        BudgetDAO.addBudget(1, 1500.00, "May 2025");

// Fetch and print all budgets for user 1
        for (String b : BudgetDAO.getBudgetsByUserId(1)) {
            System.out.println(b);
        }
        // Test login (user must already exist)
        boolean success = UserDAO.validateLogin("mariam@example.com", "password123");

        if (success) {
            System.out.println("Logged in ✅");
        } else {
            System.out.println("Invalid credentials ❌");
        }

    }
}
