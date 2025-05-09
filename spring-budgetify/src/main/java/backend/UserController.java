package com.example.budgetify.controller;

import com.example.budgetify.dao.UserDAO;
import com.example.budgetify.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping
    public List<String> getAllUsers() {
        return UserDAO.getAllUsers();
    }

    @PostMapping
    public void addUser(@RequestBody User user) {
        UserDAO.addUser(user.getEmail(), user.getName(), user.getPassword());
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        UserDAO.deleteUser(userId);
    }

    @PostMapping("/login")
    public boolean validateLogin(@RequestBody User user) {
        return UserDAO.validateLogin(user.getEmail(), user.getPassword());
    }
}
