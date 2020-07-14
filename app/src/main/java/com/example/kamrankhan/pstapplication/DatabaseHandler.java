package com.example.kamrankhan.pstapplication;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "PSTdatabase.db";

    // Contacts table name
    private static final String TABLE_INBOX = "Inbox";

    // Contacts Table Columns names
    private static final String KEY_EMAIL_ID = "emailID";
    private static final String KEY_SENDER_NAME = "senderName";
    private static final String KEY_SENDER_SUBJECT = "senderSubject";
    private static final String KEY_SENDER_DATE = "senderDate";
    private static final String KEY_MSG_BODY = "messageBody";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_INBOX_TABLE = "CREATE TABLE " + TABLE_INBOX + "("
                + KEY_EMAIL_ID + " INTEGER PRIMARY KEY AUTOICREMENT," + KEY_SENDER_NAME + " TEXT,"
                + KEY_SENDER_SUBJECT + " TEXT"
                + KEY_SENDER_DATE + " TEXT"
                + KEY_MSG_BODY + " TEXT"
                + ")";

        db.execSQL(CREATE_INBOX_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INBOX);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addChildFolder(ChildFolders childFolders) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName()); // Contact Name
        //values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone

        values.put(KEY_SENDER_NAME, childFolders.getSender()); // Sender Name
        values.put(KEY_SENDER_SUBJECT, childFolders.getSubject()); // email Subject
        values.put(KEY_SENDER_DATE, childFolders.getTime()); // email sent time
        values.put(KEY_MSG_BODY, childFolders.getMsgBody()); // enail message

        // Inserting Row
        db.insert(TABLE_INBOX, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    ChildFolders getChildFolder(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_INBOX, new String[] { KEY_EMAIL_ID,
                        KEY_SENDER_NAME, KEY_SENDER_SUBJECT,  KEY_SENDER_DATE, KEY_MSG_BODY}, KEY_EMAIL_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ChildFolders childFolders = new ChildFolders(Long.parseLong(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        // return contact
        return childFolders;
    }

    // Getting All Contacts
    public ArrayList<ChildFolders> getAllChildFolders() {
        ArrayList<ChildFolders> childFoldersArrayList = new ArrayList<ChildFolders>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_INBOX;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChildFolders childFolders = new ChildFolders();
                childFolders.setId(Long.parseLong(cursor.getString(0)));
                childFolders.setSender(cursor.getString(1));
                childFolders.setSubject(cursor.getString(2));
                childFolders.setTime(cursor.getString(3));
                childFolders.setMsgBody(cursor.getString(4));

                // Adding contact to list
                childFoldersArrayList.add(childFolders);
            } while (cursor.moveToNext());
        }

        // return contact list
        return childFoldersArrayList;
    }

    // Updating single childfolder (email)
    public int updateChildFolder(ChildFolders childFolders) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SENDER_NAME, childFolders.getSender()); // Sender Name
        values.put(KEY_SENDER_SUBJECT, childFolders.getSubject()); // email Subject
        values.put(KEY_SENDER_DATE, childFolders.getTime()); // email sent time
        values.put(KEY_MSG_BODY, childFolders.getMsgBody()); // enail message

        // updating row
        return db.update(TABLE_INBOX, values, KEY_EMAIL_ID + " = ?",
                new String[] { String.valueOf(childFolders.getId()) });
    }

    // Deleting single childfolder (email)
    public void deleteChildFolder(ChildFolders childFolders) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INBOX, KEY_EMAIL_ID + " = ?",
                new String[] { String.valueOf(childFolders.getId()) });
        db.close();
    }


    // Getting Inbox Count
    public int getInboxCount() {
        String countQuery = "SELECT  * FROM " + TABLE_INBOX;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}

