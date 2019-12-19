/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.DatabaseException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidBalanceException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

/**
 * This is an In-Memory implementation of the AccountDAO interface. This is not a persistent storage. A HashMap is
 * used to store the account details temporarily in the memory.
 */
public class InMemoryAccountDAO implements AccountDAO {
    private final Map<String, Account> accounts;
    public DBHelper db;

    public InMemoryAccountDAO(DBHelper db) {

        this.accounts = new HashMap<>();
        this.db =  db;
        setup();

    }

    private void setup(){
        List temp = getAccountsList();
        if (temp.size()!=0){
            for (int i =0 ; i < temp.size(); i++){
                Account a = (Account) (temp.get(i));
                this.accounts.put (a.getAccountNo(),a);
            }
        }
    }

    @Override
    public List<String> getAccountNumbersList() {
        Cursor resultSet = this.db.getAllAccount();
        ArrayList temp = new ArrayList<Account>();
        if (resultSet.getCount()==0){
            return temp;
        }
        else{

            while (resultSet.moveToNext()){
                if (resultSet.getString(4)!="1"){
                    temp.add(resultSet.getString(0));
                }
            }
        }
        return temp;
    }

    @Override
    public List<Account> getAccountsList() {

        Cursor resultSet = this.db.getAllAccount();
        ArrayList temp = new ArrayList<Account>();
        if (resultSet.getCount()==0){
            return temp;
        }
        else{

            while (resultSet.moveToNext()){
                if (resultSet.getString(4)!="1"){
                    Account a = new Account(resultSet.getString(0),resultSet.getString(1),resultSet.getString(2),resultSet.getDouble(3));
                    temp.add(a);
                }
            }
        }
        return temp;
//
//
//        resultSet.moveToFirst();
//        return new ArrayList<>(accounts.values());
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        if (accounts.containsKey(accountNo)) {
            return accounts.get(accountNo);
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) throws InvalidAccountException {
        boolean b = db.insertDataIntoAccount(account);
        if (b){
            accounts.put(account.getAccountNo(), account);
        }
        else{
            String msg = "Account Creation Failed. Try Again";
            throw new InvalidAccountException(msg);
        }

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException, DatabaseException {


        if (!accounts.containsKey(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        boolean c = this.db.removeAccount(accountNo);
        if (c) {
            accounts.remove(accountNo);
        }
        else{
            String msg = "Account Deletion Failed. Try Again";
            throw new DatabaseException(msg);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException, InvalidBalanceException, DatabaseException {
        if (!accounts.containsKey(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        Account account = accounts.get(accountNo);
        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                double balance = account.getBalance() - amount;
                if (balance < 0){
                    String msg = "Invalid Balance";
                    throw new InvalidBalanceException(msg);
                }
                else{
                    boolean c = this.db.updateBalance(accountNo,balance);
                    if (c) {
                        account.setBalance(account.getBalance() - amount);
                    }
                    else{
                        String msg = "Balance Updation Failed. Try Again";
                        throw new DatabaseException(msg);
                    }
                }
                break;
            case INCOME:

                double income = account.getBalance() + amount;
                boolean c = this.db.updateBalance(accountNo,income);
                if (c) {
                    account.setBalance(account.getBalance() + amount);
                    System.out.println("ABABABA");
                    System.out.println(income);
                }
                else{
                    String msg = "Balance Updation Failed. Try Again";
                    throw new DatabaseException(msg);
                }
                break;
        }
        accounts.put(accountNo, account);
    }
}
