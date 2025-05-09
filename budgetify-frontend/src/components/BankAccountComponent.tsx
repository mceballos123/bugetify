import React, { useState, useEffect } from 'react';
import axios from 'axios';

interface BankAccount {
  accountId: number;
  userId: number;
  accountName: string;
  firstName: string;
  lastName: string;
  dateOpened: string;
  numberWithdrawals: number;
  numberDeposits: number;
  provider: string;
}

const BankAccountComponent: React.FC = () => {
  const [accounts, setAccounts] = useState<BankAccount[]>([]);
  const [newAccount, setNewAccount] = useState<BankAccount>({
    accountId: 0,
    userId: 1, // Default userId for testing
    accountName: '',
    firstName: '',
    lastName: '',
    dateOpened: '',
    numberWithdrawals: 0,
    numberDeposits: 0,
    provider: '',
  });

  useEffect(() => {
    fetchAccounts();
  }, []);

  const fetchAccounts = async () => {
    try {
      const response = await axios.get<BankAccount[]>('http://localhost:8080/api/bankaccounts');
      setAccounts(response.data);
    } catch (error) {
      console.error('Error fetching bank accounts:', error);
    }
  };

  const addAccount = async () => {
    try {
      await axios.post('http://localhost:8080/api/bankaccounts', newAccount);
      fetchAccounts();
    } catch (error) {
      console.error('Error adding bank account:', error);
    }
  };

  const deleteAccount = async (accountId: number) => {
    try {
      await axios.delete(`http://localhost:8080/api/bankaccounts/${accountId}`);
      fetchAccounts();
    } catch (error) {
      console.error('Error deleting bank account:', error);
    }
  };

  return (
    <div>
      <h2>Bank Accounts</h2>
      <ul>
        {accounts.map(account => (
          <li key={account.accountId}>
            {account.accountName} - {account.provider}
            <button onClick={() => deleteAccount(account.accountId)}>Delete</button>
          </li>
        ))}
      </ul>
      <div>
        <input
          type="text"
          placeholder="Account Name"
          value={newAccount.accountName}
          onChange={e => setNewAccount({ ...newAccount, accountName: e.target.value })}
        />
        <input
          type="text"
          placeholder="First Name"
          value={newAccount.firstName}
          onChange={e => setNewAccount({ ...newAccount, firstName: e.target.value })}
        />
        <input
          type="text"
          placeholder="Last Name"
          value={newAccount.lastName}
          onChange={e => setNewAccount({ ...newAccount, lastName: e.target.value })}
        />
        <input
          type="date"
          placeholder="Date Opened"
          value={newAccount.dateOpened}
          onChange={e => setNewAccount({ ...newAccount, dateOpened: e.target.value })}
        />
        <input
          type="number"
          placeholder="Number of Withdrawals"
          value={newAccount.numberWithdrawals}
          onChange={e => setNewAccount({ ...newAccount, numberWithdrawals: parseInt(e.target.value) })}
        />
        <input
          type="number"
          placeholder="Number of Deposits"
          value={newAccount.numberDeposits}
          onChange={e => setNewAccount({ ...newAccount, numberDeposits: parseInt(e.target.value) })}
        />
        <input
          type="text"
          placeholder="Provider"
          value={newAccount.provider}
          onChange={e => setNewAccount({ ...newAccount, provider: e.target.value })}
        />
        <button onClick={addAccount}>Add Bank Account</button>
      </div>
    </div>
  );
};

export default BankAccountComponent;