package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.DBHelper;

public class  PersistentMemoryExpenseManager extends ExpenseManager{

    DBHelper db;

    public PersistentMemoryExpenseManager(DBHelper db) throws ParseException {
        this.db = db;
        setup();
    }

    @Override
    public void setup() throws ParseException {
        /*** Begin generating dummy data for In-Memory implementation ***/

        TransactionDAO PersistentMemoryTransactionDAO = new PersistentMemoryTransactionDAO(this.db);
        setTransactionsDAO(PersistentMemoryTransactionDAO);

        AccountDAO PersistentMemoryAccountDAO = new PersistentMemoryAccountDAO(this.db);
        setAccountsDAO(PersistentMemoryAccountDAO);

        // dummy data
        Account dummyAcct1 = new Account("12346A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78949Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);

        try {
            getAccountsDAO().addAccount(dummyAcct1);
            getAccountsDAO().addAccount(dummyAcct2);
        } catch (InvalidAccountException e) {
            e.printStackTrace();
        }

        /*** End ***/
    }
}