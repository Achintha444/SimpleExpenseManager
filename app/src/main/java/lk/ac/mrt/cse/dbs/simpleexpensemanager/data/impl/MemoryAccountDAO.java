package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class MemoryAccountDAO implements AccountDAO {

    private final Map<String, Account> accounts;
    public SQLiteDatabase db;

    public MemoryAccountDAO(SQLiteDatabase db) {
        this.accounts = new HashMap<>();
        this.db = db;
    }

    @Override
    public List<String> getAccountNumbersList() {
        return null;
    }

    @Override
    public List<Account> getAccountsList() {
        return null;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        return null;
    }

    @Override
    public void addAccount(Account account) {

        ContentValues cv = new ContentValues();
        cv.put("account_number",account.getAccountNo());
        cv.put("bank",account.getBankName());
        cv.put("accountHolder",account.getAccountHolderName());
        cv.put("balance",account.getBalance());
        cv.put("deleted",false);
        db.insert("account",null,cv);
        accounts.put(account.getAccountNo(), account);


    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

    }
}
