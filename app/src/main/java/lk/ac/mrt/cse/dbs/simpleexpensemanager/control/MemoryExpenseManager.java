package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.MemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.MemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class MemoryExpenseManager extends ExpenseManager{

    public SQLiteDatabase db;

    public MemoryExpenseManager(SQLiteDatabase db) {
        this.db = db;
        setup();
    }

    @Override
    public void setup() {

        TransactionDAO inMemoryTransactionDAO = new MemoryTransactionDAO(this.db);
        setTransactionsDAO(inMemoryTransactionDAO);

        AccountDAO inMemoryAccountDAO = new MemoryAccountDAO(this.db);
        setAccountsDAO(inMemoryAccountDAO);

        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        try {
            getAccountsDAO().addAccount(dummyAcct1);
            getAccountsDAO().addAccount(dummyAcct2);
        } catch (InvalidAccountException e) {

        }
    }
}
