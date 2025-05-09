import React, { useState, useEffect } from 'react';
import axios from 'axios';

interface Budget {
  budgetId: number;
  userId: number;
  monthlyBudget: number;
  month: string;
}

const BudgetComponent: React.FC = () => {
  const [budgets, setBudgets] = useState<Budget[]>([]);
  const [newBudget, setNewBudget] = useState<Budget>({
    budgetId: 0,
    userId: 1, // Default userId for testing
    monthlyBudget: 0,
    month: '',
  });

  useEffect(() => {
    fetchBudgets();
  }, []);

  const fetchBudgets = async () => {
    try {
      const response = await axios.get<Budget[]>('http://localhost:8080/api/budgets');
      setBudgets(response.data);
    } catch (error) {
      console.error('Error fetching budgets:', error);
    }
  };

  const addBudget = async () => {
    try {
      await axios.post('http://localhost:8080/api/budgets', newBudget);
      fetchBudgets();
    } catch (error) {
      console.error('Error adding budget:', error);
    }
  };

  const deleteBudget = async (budgetId: number) => {
    try {
      await axios.delete(`http://localhost:8080/api/budgets/${budgetId}`);
      fetchBudgets();
    } catch (error) {
      console.error('Error deleting budget:', error);
    }
  };

  return (
    <div>
      <h2>Budgets</h2>
      <ul>
        {budgets.map(budget => (
          <li key={budget.budgetId}>
            {budget.month} - ${budget.monthlyBudget}
            <button onClick={() => deleteBudget(budget.budgetId)}>Delete</button>
          </li>
        ))}
      </ul>
      <div>
        <input
          type="number"
          placeholder="Monthly Budget"
          value={newBudget.monthlyBudget}
          onChange={e => setNewBudget({ ...newBudget, monthlyBudget: parseFloat(e.target.value) })}
        />
        <input
          type="text"
          placeholder="Month"
          value={newBudget.month}
          onChange={e => setNewBudget({ ...newBudget, month: e.target.value })}
        />
        <button onClick={addBudget}>Add Budget</button>
      </div>
    </div>
  );
};

export default BudgetComponent;