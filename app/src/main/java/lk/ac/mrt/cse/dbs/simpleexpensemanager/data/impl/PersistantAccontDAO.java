package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.ApplicationContext;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Dulaj on 03-Dec-15.
 */

public class PersistantAccontDAO implements AccountDAO{

    private final DatabaseHandler databaseHandler = new DatabaseHandler(ApplicationContext.getContext());

    @Override
    public List<String> getAccountNumbersList() {
        return databaseHandler.getAccountNumbers();
    }

    @Override
    public List<Account> getAccountsList() {
        return databaseHandler.getAllAccounts();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account = databaseHandler.getAccount(accountNo);
        if(account!=null){
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        databaseHandler.addAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        databaseHandler.removeAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = databaseHandler.getAccount(accountNo);
        switch (expenseType) {
            case EXPENSE:
                databaseHandler.updateBalance(accountNo, account.getBalance() - amount);
                break;
            case INCOME:
                databaseHandler.updateBalance(accountNo, account.getBalance() + amount);
                break;
        }
    }
}
