package Managers;

import Models.Transaction;
import Models.TransactionDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class TransactionManager extends Observable {
    private List<Transaction> transactions = new ArrayList<>();
    private TransactionDAO transactionDAO = new TransactionDAO();

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        setChanged();
        notifyObservers(transactions);
    }

    public void addTransaction(Transaction transaction) {
        transactionDAO.insertTransaction(transaction);
        transactions.add(transaction);
        setChanged();
        notifyObservers(transactions);
    }

    public void loadTransactionsFromDatabase() {
        List<Transaction> transactionsFromDB = transactionDAO.getAllTransactions(true);
        setTransactions(transactionsFromDB);
    }
}
