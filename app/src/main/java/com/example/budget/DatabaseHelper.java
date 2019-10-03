package com.example.budget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "budget.db";
    public static String TABLE_NAME = "envelope_data";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Name";
    public static final String COL_3 = "Amount";

    public DatabaseHelper(Context context){

        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS register_data (ID INTEGER PRIMARY KEY AUTOINCREMENT, ENVID LONG, DATE TEXT, PAYEE TEXT, AMOUNT FLOAT, BALANCE FLOAT)";
        db.execSQL("create table IF NOT EXISTS envelope_data (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, AMOUNT FLOAT)");
        db.execSQL("create table if not exists account_data (ID integer primary key autoincrement, name TEXT, balance FLOAT, available_credit FLOAT, credit_limit FLOAT, isCC TEXT)");
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS envelope_data");
        db.execSQL("DROP TABLE IF EXISTS register_data");
        db.execSQL("DROP TABLE IF EXISTS account_data");
        onCreate(db);
    }

    public long insertAccountData(String name, Float balance, Float available_credit, Float credit_limit, String isCC){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("create table if not exists account_data (ID integer primary key autoincrement, name TEXT, balance FLOAT, available_credit FLOAT, credit_limit FLOAT, isCC TEXT)");
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("balance", balance);
        cv.put("available_credit", available_credit);
        cv.put("credit_limit", credit_limit);
        cv.put("isCC", isCC);
        long insertResult = db.insert("account_data",null,cv);
        return insertResult;
    }

    public Cursor getAllAccounts(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("create table if not exists account_data (ID integer primary key autoincrement, name TEXT, balance FLOAT, available_credit FLOAT, credit_limit FLOAT, isCC TEXT)");
        Cursor data = db.rawQuery("select * from account_data order by name collate nocase asc", null);
        return data;
    }

    public Cursor getSpecificAccount(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("create table if not exists account_data (ID integer primary key autoincrement, name TEXT, balance FLOAT, available_credit FLOAT, credit_limit FLOAT, isCC TEXT)");
        Cursor data = db.rawQuery("select * from account_data where id="+id, null);
        return data;
    }

    public int updateAccountData(int id, String name, Float balance, Float available_credit, float credit_limit){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("create table if not exists account_data (ID integer primary key autoincrement, name TEXT, balance FLOAT, available_credit FLOAT, credit_limit FLOAT, isCC TEXT)");
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("balance", balance);
        cv.put("available_credit", available_credit);
        cv.put("credit_limit", credit_limit);

        int update = db.update("account_data", cv,"id=" + id,null);
        return update;
    }

    public int deleteAccountData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete("account_data","id=" + id,null);
        return delete;
    }


    public long insertEnvelopeData(String name, Float amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, amount);
        long result = db.insert("envelope_data", null, contentValues);
        return result;
    }

    public long insertRegisterData(Long envid, String date, String payee, Float amount, Float balance){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS register_data (ID INTEGER PRIMARY KEY AUTOINCREMENT, ENVID LONG, DATE TEXT, PAYEE TEXT, AMOUNT FLOAT, BALANCE FLOAT)");

        ContentValues cv = new ContentValues();
        cv.put("ENVID", envid);
        cv.put("DATE", date);
        cv.put("PAYEE", payee);
        cv.put("AMOUNT", amount);
        cv.put("BALANCE", balance);
        long result = db.insert("register_data", null, cv);
        return result;
    }

    public Cursor getAllEnvelopeData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from envelope_data order by name collate nocase asc", null);
        return res;
    }

    public Cursor getEnvelopeData(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        TABLE_NAME = "envelope_data";
        Cursor res = db.rawQuery("select * from envelope_data where ID=" + id, null);
        return res;
    }

    public Cursor GetRegisterDataUnSorted(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS register_data (ID INTEGER PRIMARY KEY AUTOINCREMENT, ENVID LONG, DATE TEXT, PAYEE TEXT, AMOUNT FLOAT, BALANCE FLOAT)");
        TABLE_NAME = "register_data";
        Cursor res = db.rawQuery("select * from register_data where ENVID=" + id, null);
        return res;
    }

    public Cursor getRegisterData(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS register_data (ID INTEGER PRIMARY KEY AUTOINCREMENT, ENVID LONG, DATE TEXT, PAYEE TEXT, AMOUNT FLOAT, BALANCE FLOAT)");
        TABLE_NAME = "register_data";
        Cursor res = db.rawQuery("select * from register_data where ENVID=" + id + " order by id desc", null);
        return res;
    }

    public Cursor getRegisterData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS register_data (ID INTEGER PRIMARY KEY AUTOINCREMENT, ENVID LONG, DATE TEXT, PAYEE TEXT, AMOUNT FLOAT, BALANCE FLOAT)");
        TABLE_NAME = "register_data";
        Cursor res = db.rawQuery("select * from register_data where id=" + id, null);
        return res;
    }

    public int deleteEnvelopeData(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete("envelope_data", "ID =" + id, null);
        return res;
    }
    public int deleteRegisterData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete("register_data", "ID =" + id, null);
        return res;
    }

    public int updateEnvelopeData(Long ENVid, String name, Float amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("NAME", name);
        cv.put("AMOUNT", amount);
        int res = db.update("envelope_data", cv, "ID="+ENVid, new String[]{});

        return res;
    }

    public int updateEnvelopeBalance(Long ENVid, Float amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("AMOUNT", amount);
        int res = db.update("envelope_data", cv, "ID="+ENVid, new String[]{});

        return res;
    }

    public Cursor updateRegisterData(int idValue, String date, String payee, float amount, float balance){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "update register_data set date='" + date + "', payee='"+payee+"', amount="+amount+", balance="+balance+" where ID=" + idValue;

        Cursor res = db.rawQuery(query, null);

        return res;
    }

    public Cursor updateRegisterStartingData(Long idValue, Float balance){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("AMOUNT", balance);
        cv.put("BALANCE", balance);

        String query = "update register_data set amount=" + balance +", balance=" + balance + " where envid=" + idValue + " and payee='STARTING'";

        Cursor res = db.rawQuery(query, null);
        return res;
    }

    public Cursor updateRegisterBalance(int idValue, Float balance){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "update register_data set balance=" + balance + " where id=" + idValue;

        Cursor res = db.rawQuery(query, null);
        return res;
    }

    public int updateAccountBalance(int id, Float balance){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("create table if not exists account_data (ID integer primary key autoincrement, name TEXT, balance FLOAT, available_credit FLOAT, credit_limit FLOAT, isCC TEXT)");
        ContentValues cv = new ContentValues();
        cv.put("balance", balance);

        int update = db.update("account_data", cv,"id=" + id,null);
        return update;
    }
}
