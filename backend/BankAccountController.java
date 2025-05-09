package com.example.budgetify.controller;

// Import the BankAccount class from the model package
import com.example.budgetify.model.BankAccount;
import com.example.budgetify.dao.BankAccountDAO;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bankaccounts")
public class BankAccountController {

    @GetMapping
    public List<String> getAllBankAccounts(@RequestParam int userId) {
        return BankAccountDAO.getBankAccountsByUserId(userId);
    }

    @PostMapping
    public void addBankAccount(@RequestBody BankAccount bankAccount) {
        BankAccountDAO.addBankAccount(
                bankAccount.getUserId(),
                bankAccount.getAccountName(),
                bankAccount.getFirstName(),
                bankAccount.getLastName(),
                bankAccount.getDateOpened(),
                bankAccount.getNumberWithdrawals(),
                bankAccount.getNumberDeposits(),
                bankAccount.getProvider());
    }

    @PutMapping("/{accountId}")
    public void updateBankAccount(@PathVariable int accountId, @RequestBody BankAccount bankAccount) {
        BankAccountDAO.updateBankAccount(
                accountId,
                bankAccount.getAccountName(),
                bankAccount.getFirstName(),
                bankAccount.getLastName(),
                bankAccount.getDateOpened(),
                bankAccount.getNumberWithdrawals(),
                bankAccount.getNumberDeposits(),
                bankAccount.getProvider());
    }

    @DeleteMapping("/{accountId}")
    public void deleteBankAccount(@PathVariable int accountId) {
        BankAccountDAO.deleteBankAccount(accountId);
    }
}
