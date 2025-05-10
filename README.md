# Budgetify: A Student Budgeting Application

**Authors:**
- Sherine Aldrin (017197492) – sherine.aldrin@sjsu.edu  
- Mariam Jamil – mariam.jamil@sjsu.edu  
- Martin Ceballos (015476370) – martin.ceballos@sjsu.edu  

**Date:** May 9, 2025

---

## 📌 Introduction

**Budgetify** is a budgeting application designed for students and individuals who want to effectively manage their monthly finances. It enables users to input and track transactions, view monthly budgets, and analyze financial activity through charts. The goal is to empower users to save better and spend wisely.

---

## 🎯 Objectives

- **Primary Goal:** Help users manage their finances by providing a platform to log and monitor transactions against a monthly budget.
- **Key Features:**
  - User registration and login
  - Transaction and budget management
  - Visual financial insights via charts
  - Secure data handling and persistent storage
  - CRUD operations for users, budgets, transactions, and bank accounts

---

## 🏗️ System Architecture

**Three-Tier Structure:**
1. **Presentation Layer:** React front-end UI
2. **Logic Layer:** Spring Boot backend application
3. **Database Layer:** SQLite database accessed using JDBC

**Tech Stack:**
- **Frontend:** React
- **Backend:** Java + Spring Boot
- **Database:** SQLite using JDBC

---

## 🔧 Setup Instructions

### 🖥️ Backend Setup (`/backend`)
Navigate to the backend folder and run the Spring Boot application using Maven:

## Frontend SetUp
cd ../frontend
npm install
npm run dev
```bash
cd backend
mvn spring-boot:run

