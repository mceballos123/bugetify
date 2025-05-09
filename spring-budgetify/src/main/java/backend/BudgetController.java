package com.example.budgetify.controller;

import com.example.budgetify.dao.BudgetDAO;
import com.example.budgetify.model.Budget;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    @GetMapping
    public List<String> getAllBudgets(@RequestParam int userId) {
        return BudgetDAO.getBudgetsByUserId(userId);
    }

    @PostMapping
    public void addBudget(@RequestBody Budget budget) {
        BudgetDAO.addBudget(budget.getUserId(), budget.getMonthlyBudget(), budget.getMonth());
    }

    @PutMapping("/{budgetId}")
    public void updateBudget(@PathVariable int budgetId, @RequestBody Budget budget) {
        BudgetDAO.updateBudget(budgetId, budget.getMonthlyBudget(), budget.getMonth());
    }

    @DeleteMapping("/{budgetId}")
    public void deleteBudget(@PathVariable int budgetId) {
        BudgetDAO.deleteBudget(budgetId);
    }
}