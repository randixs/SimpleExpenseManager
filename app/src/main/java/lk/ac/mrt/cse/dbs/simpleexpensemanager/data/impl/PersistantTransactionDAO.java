package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.ApplicationContext;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Dulaj on 03-Dec-15.
 */
public class PersistantTransactionDAO implements TransactionDAO {

    private final DatabaseHandler databaseHandler = new DatabaseHandler(ApplicationContext.getContext());

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        databaseHandler.addTransaction(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return databaseHandler.getAllTransactionLogs();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return databaseHandler.getLastNLogs(limit);
    }
}
