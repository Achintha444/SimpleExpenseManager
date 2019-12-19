package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "170638D", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS account (account_number CHAR(6) NOT NULL, bank VARCHAR(100) NOT NULL, accountHolder VARCHAR(200) NOT NULL, balance numeric (10,2) NOT NULL CHECK(balance >= 0), deleted BOOLEAN NOT NULL, PRIMARY KEY(account_number));");
        //Created the tranasaction table to keep track of the tranasaction logs.
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS transaction_detail (transaction_id INTEGER  PRIMARY KEY AUTOINCREMENT,date date NOT NULL, account_number CHAR(6) NOT NULL, type CHAR(7) NOT NULL CHECK(type IN ('EXPENSE','INCOME')), amount numeric (10,2) NOT NULL, FOREIGN KEY (account_number) REFERENCES account(account_number)); ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS account");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS transaction_detail");
        onCreate(sqLiteDatabase);
    }

    public boolean insertDataIntoAccount (Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("account_number",account.getAccountNo());
        cv.put("bank",account.getBankName());
        cv.put("accountHolder",account.getAccountHolderName());
        cv.put("balance",account.getBalance());
        cv.put("deleted",false);
        long res = db.insert("account",null,cv);
        if (res == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor getAllAccount(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM account",null);
        return res;
    }

    public boolean removeAccount(String account_number){
        SQLiteDatabase db = getWritableDatabase();
        long res = db.delete("account","account_number = ?", new String[]{account_number});
        if (res == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean updateBalance (String account_no, Double balance){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("balance",balance); //These Fields should be your String values of actual column names
        long res = db.update("account",cv,"account_number = ?",new String[] {account_no});
        if (res == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean logTransaction(Transaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date",transaction.getDate().toString());
        cv.put("account_number",transaction.getAccountNo());
        cv.put("type",transaction.getExpenseType().toString());
        cv.put("amount",transaction.getAmount());
        long res = db.insert("transaction_detail",null,cv);
        if (res == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor getAllTransactionLogs(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM transaction_detail",null);
        return res;
    }
}
