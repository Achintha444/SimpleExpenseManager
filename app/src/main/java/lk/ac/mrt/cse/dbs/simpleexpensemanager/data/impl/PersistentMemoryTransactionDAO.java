package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.DatabaseException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class  PersistentMemoryTransactionDAO implements TransactionDAO {
    private final List<Transaction> transactions;
    public DBHelper db;

    public PersistentMemoryTransactionDAO(DBHelper db) throws ParseException {

        this.transactions = new LinkedList<>();
        this.db = db;
        setup();
    }

    private void setup() throws ParseException {
        List temp = getAllTransactionLogs();
        if (temp.size()!=0){
            for (int i =0 ; i < temp.size(); i++){
                Transaction t = (Transaction) (temp.get(i));
                this.transactions.add (t);
            }
        }
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) throws DatabaseException {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        boolean c = this.db.logTransaction(transaction);
        if (c) {
            transactions.add(transaction);
        } else {
            String msg = "Transaction Added Failed";
            throw new DatabaseException(msg);
        }

    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {

        Cursor resultSet = this.db.getAllTransactionLogs();
        ArrayList temp = new ArrayList<Transaction>();
        if (resultSet.getCount() == 0) {
            return temp;
        } else {

            while (resultSet.moveToNext()) {
                Transaction t = new Transaction(new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH).parse(resultSet.getString(1)), resultSet.getString(2), (ExpenseType.valueOf(resultSet.getString(3))), resultSet.getDouble(4));
                temp.add(t);
            }
        }
        return temp;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }

}