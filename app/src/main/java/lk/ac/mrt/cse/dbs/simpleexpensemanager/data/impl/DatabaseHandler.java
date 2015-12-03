package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Dulaj on 03-Dec-15.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static String DATABASE_NAME = "expenses.db";

    // Table names
    private static String TABLE_ACCOUNT = "account";
    private static String TABLE_TRANSACTION = "transactions";

    // Table Columns name
    private static final String KEY_ACCOUNT_NO = "account_no";
    private static final String KEY_BANK_NAME = "bank_name";
    private static final String KEY_HOLDER_NAME = "holder_name";
    private static final String KEY_BALANCE = "balance";

    private static final String KEY_DATE = "date";
    private static final String KEY_EXPENSE_TYPE = "expense_type";
    private static final String KEY_AMOUNT = "amount";

    // Create queries
    private static final String CREATE_TABLE_ACCOUTN = "CREATE TABLE " + TABLE_ACCOUNT + "(" + KEY_ACCOUNT_NO + " TEXT NOT NULL, " + KEY_BANK_NAME + " TEXT NOT NULL, " + KEY_HOLDER_NAME + " TEXT NOT NULL, " + KEY_BALANCE + " REAL NOT NULL)";
    private static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TABLE_TRANSACTION + "(" + KEY_DATE + " TEXT NOT NULL, " + KEY_ACCOUNT_NO + " TEXT NOT NULL, " + KEY_EXPENSE_TYPE + " TEXT NOT NULL, " + KEY_AMOUNT + " REAL NOT NULL)";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("Query 1", CREATE_TABLE_ACCOUTN);
        Log.i("Query 2", CREATE_TABLE_TRANSACTION);
        db.execSQL(CREATE_TABLE_ACCOUTN);
        db.execSQL(CREATE_TABLE_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ACCOUTN);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TRANSACTION);
    }

    // Functions for accounts table
    public List<String> getAccountNumbers(){
        List<String> accountNoList = new ArrayList<String>();
        String selectQuery="SELECT " + KEY_ACCOUNT_NO + " FROM " + TABLE_ACCOUNT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            do {
                accountNoList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return accountNoList;
    }

    public List<Account> getAllAccounts(){
        List<Account> accountList = new ArrayList<Account>();
        String selectQuery="SELECT * FROM " + TABLE_ACCOUNT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
                accountList.add(account);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return accountList;
    }

    public Account getAccount(String accountNo){
        Account account=null;
        String selectQuery="SELECT * FROM " + TABLE_ACCOUNT + " WHERE " + KEY_ACCOUNT_NO + "=" + accountNo;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            do {
                account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return account;
    }

    public void addAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ACCOUNT_NO, account.getAccountNo());
        values.put(KEY_BANK_NAME, account.getBankName());
        values.put(KEY_HOLDER_NAME, account.getAccountHolderName());
        values.put(KEY_BALANCE, account.getBalance());

        db.insert(TABLE_ACCOUNT, null, values);
        db.close();
    }

    public void removeAccount(String accountNo){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACCOUNT, KEY_ACCOUNT_NO + "=" + accountNo, null);
        db.close();
    }

    public void updateBalance(String accountNo, double newAmount){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BALANCE, newAmount);

        db.update(TABLE_ACCOUNT, values, KEY_ACCOUNT_NO + "=" + accountNo, null);
        db.close();
    }

    // Functions for Transactions table
    public void addTransaction(Transaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, transaction.getDate().toString());
        values.put(KEY_HOLDER_NAME, transaction.getAccountNo());
        values.put(KEY_EXPENSE_TYPE, transaction.getExpenseType().toString());
        values.put(KEY_AMOUNT, transaction.getAmount());

        db.insert(TABLE_TRANSACTION, null, values);
        db.close();
    }

    public List<Transaction> getAllTransactionLogs(){
        List<Transaction> transactionList = new ArrayList<Transaction>();
        String selectQuery="SELECT * FROM " + TABLE_TRANSACTION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            do {
                Date date = null;
                String dateString = cursor.getString(0) + "T00:00:00Z";
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                try {
                    date = format.parse(dateString);
                    System.out.println(date);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    Log.e("Database Handler", "Error converting String to Date. " + e.toString());
                }

                Transaction transaction = new Transaction(date, cursor.getString(1), ExpenseType.valueOf(cursor.getString(2)), cursor.getDouble(3));
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return transactionList;
    }

    public List<Transaction> getLastNLogs(int limit){
        List<Transaction> transactionList = new ArrayList<Transaction>();
        String selectQuery="SELECT * FROM " + TABLE_TRANSACTION + " ORDER BY " + KEY_DATE + " ASC LIMIT " + limit;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            do {
                Date date = null;
                String dateString = cursor.getString(0) + "T00:00:00Z";
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                try {
                    date = format.parse(dateString);
                    System.out.println(date);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    Log.e("Database Handler", "Error converting String to Date. " + e.toString());
                }

                Transaction transaction = new Transaction(date, cursor.getString(1), ExpenseType.valueOf(cursor.getString(2)), cursor.getDouble(3));
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return transactionList;
    }

}
