import React from 'react';
import './App.css'; // Import the CSS file
import UserComponent from './components/UserComponent';
import TransactionComponent from './components/TransactionComponent';
import BudgetComponent from './components/BudgetComponent';
import BankAccountComponent from './components/BankAccountComponent';

const App: React.FC = () => {
  return (
    <div>
      <h1>Budgetify</h1>
      <UserComponent />
      <TransactionComponent />
      <BudgetComponent />
      <BankAccountComponent />
    </div>
  );
};

export default App;