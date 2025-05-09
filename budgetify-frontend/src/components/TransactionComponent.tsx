import React, { useState, useEffect } from 'react';
import axios from 'axios';

interface Transaction {
  transactionId: number;
  userId: number;
  title: string;
  amount: number;
  category: string;
  date: string;
  method: string;
}

const TransactionComponent: React.FC = () => {
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [newTransaction, setNewTransaction] = useState<Transaction>({
    transactionId: 0,
    userId: 1, // Default userId for testing
    title: '',
    amount: 0,
    category: '',
    date: '',
    method: '',
  });

  useEffect(() => {
    fetchTransactions();
  }, []);

  const fetchTransactions = async () => {
    try {
      const response = await axios.get<Transaction[]>('http://localhost:8080/api/transactions');
      setTransactions(response.data);
    } catch (error) {
      console.error('Error fetching transactions:', error);
    }
  };

  const addTransaction = async () => {
    try {
      await axios.post('http://localhost:8080/api/transactions', newTransaction);
      fetchTransactions();
    } catch (error) {
      console.error('Error adding transaction:', error);
    }
  };

  const deleteTransaction = async (transactionId: number) => {
    try {
      await axios.delete(`http://localhost:8080/api/transactions/${transactionId}`);
      fetchTransactions();
    } catch (error) {
      console.error('Error deleting transaction:', error);
    }
  };

  return (
    <div>
      <h2>Transactions</h2>
      <ul>
        {transactions.map(transaction => (
          <li key={transaction.transactionId}>
            {transaction.title} - ${transaction.amount}
            <button onClick={() => deleteTransaction(transaction.transactionId)}>Delete</button>
          </li>
        ))}
      </ul>
      <div>
        <input
          type="text"
          placeholder="Title"
          value={newTransaction.title}
          onChange={e => setNewTransaction({ ...newTransaction, title: e.target.value })}
        />
        <input
          type="number"
          placeholder="Amount"
          value={newTransaction.amount}
          onChange={e => setNewTransaction({ ...newTransaction, amount: parseFloat(e.target.value) })}
        />
        <input
          type="text"
          placeholder="Category"
          value={newTransaction.category}
          onChange={e => setNewTransaction({ ...newTransaction, category: e.target.value })}
        />
        <input
          type="date"
          placeholder="Date"
          value={newTransaction.date}
          onChange={e => setNewTransaction({ ...newTransaction, date: e.target.value })}
        />
        <input
          type="text"
          placeholder="Method"
          value={newTransaction.method}
          onChange={e => setNewTransaction({ ...newTransaction, method: e.target.value })}
        />
        <button onClick={addTransaction}>Add Transaction</button>
      </div>
    </div>
  );
};

export default TransactionComponent;