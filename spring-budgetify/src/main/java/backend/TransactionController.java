package com.example.budgetify.controller;

import com.example.budgetify.dao.TransactionDAO;
import com.example.budgetify.model.Transaction;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @GetMapping
    public List<String> getAllTransactions(@RequestParam int userId) {
        return TransactionDAO.getTransactionsByUserId(userId);
    }

    @PostMapping
    public void addTransaction(@RequestBody Transaction transaction) {
        TransactionDAO.addTransaction(
                transaction.getUserId(),
                transaction.getTitle(),
                transaction.getAmount(),
                transaction.getCategory(),
                transaction.getDate(),
                transaction.getMethod());
    }

    @PutMapping("/{transactionId}")
    public void updateTransaction(@PathVariable int transactionId, @RequestBody Transaction transaction) {
        TransactionDAO.updateTransaction(
                transactionId,
                transaction.getTitle(),
                transaction.getAmount(),
                transaction.getCategory(),
                transaction.getDate(),
                transaction.getMethod());
    }

    @DeleteMapping("/{transactionId}")
    public void deleteTransaction(@PathVariable int transactionId) {
        TransactionDAO.deleteTransaction(transactionId);
    }
}