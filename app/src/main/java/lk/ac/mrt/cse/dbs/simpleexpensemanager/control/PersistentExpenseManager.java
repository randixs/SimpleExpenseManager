package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantAccontDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 * Created by Dulaj on 03-Dec-15.
 */
public class PersistentExpenseManager extends ExpenseManager {

    public PersistentExpenseManager() {
        setup();
    }

    @Override
    public void setup() {
        TransactionDAO perTransactionDAO = new PersistantTransactionDAO();
        setTransactionsDAO(perTransactionDAO);

        AccountDAO perAccountDAO = new PersistantAccontDAO();
        setAccountsDAO(perAccountDAO);

    }
}
