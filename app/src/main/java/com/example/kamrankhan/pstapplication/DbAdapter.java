package com.example.kamrankhan.pstapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.kamrankhan.pstapplication.PST.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import static com.example.kamrankhan.pstapplication.R.id.folder;

public class DbAdapter {

    public static final String FileName = "your@email.com.pst";
    public static final String FileLocation = "data/data/com.example.kamrankhan.pstapplication/files";

    //COMMON COLUMN NAMES IN ALL TABLES
    public static final String KEY_ROWID = "_emailID";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_SENDER_EMAIL = "senderEmail";
    public static final String KEY_SENDER_SUBJECT = "senderSubject";
    public static final String KEY_SENDER_DATE = "senderDate";
    public static final String KEY_MSG_BODY = "msgBody";
    public static final String KEY_CC = "CC";
    public static final String KEY_BCC = "BCC";
    public static final String KEY_TABLE_NAME = "TABLE_NAME";

    //TABLE NAMES (NOTE: TABLE NAMES MUST NOT CONTAIN ANY SPACES)
    private static final String INBOX_TABLE = "Inbox";
    private static final String DELETED_TABLE = "Deleted_Items";
    private static final String DRAFTS_TABLE = "Drafts";
    private static final String OUTBOX_TABLE = "Outbox";
    private static final String JUNK_TABLE = "Junk_E_mail";
    private static final String NOTES_TABLE = "Notes";
    private static final String JOURNAL_TABLE = "Journal";
    private static final String CONTACTS_TABLE = "Contacts";
    private static final String CALENDAR_TABLE = "Calendar";
    private static final String CONVERSATION_ACTION_SETTINGS_TABLE = "Conversation_Action_Settings";
    private static final String QUICK_STEP_SETTINGS_TABLE = "Quick_Step_Settings";
    private static final String RSS_FEEDS_TABLE = "RSS_Feeds";


    //COMMON COLUMN NAMES IN LOG TABLES
    public static final String KEY_ID = "_id";
    public static final String KEY_FROM_FOLDER = "fromFolder";
    public static final String KEY_TO_FOLDER = "toFolder";
    public static final String KEY_NEW_FOLDER_NAME = "newFolders";

    //LOG TABLE NAMES (NOTE: TABLE NAMES MUST NOT CONTAIN ANY SPACES)
    private static final String DELETED_EMAIL_TABLE = "Deleted_Email";
    private static final String DELETED_FOLDER_TABLE = "Deleted_Folder";
    private static final String COPIED_EMAIL_TABLE = "Copied_Emails";
    private static final String COPIED_FOLDER_TABLE = "Copied_Folders";
    private static final String MOVED_EMAIL_TABLE = "Moved_Email";
    private static final String MOVED_FOLDER_TABLE = "Moved_Folders";
    private static final String NEW_FOLDER_TABLE = "New_Folders";



    private static final String TAG = "PST_DbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    //DATABASE NAME
    private static final String DATABASE_NAME = "PSTDBversionTwo.db";


    //DATABASE VERSION
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    //TABLES CREATION STRINGS
    private static final String INBOX_CREATE =
            "CREATE TABLE if not exists " + INBOX_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String DELETED_CREATE =
            "CREATE TABLE if not exists " + DELETED_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String DRAFTS_CREATE =
            "CREATE TABLE if not exists " + DRAFTS_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String OUTBOX_CREATE =
            "CREATE TABLE if not exists " + OUTBOX_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String JUNK_CREATE =
            "CREATE TABLE if not exists " + JUNK_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String NOTES_CREATE =
            "CREATE TABLE if not exists " + NOTES_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String JOURNAL_CREATE =
            "CREATE TABLE if not exists " + JOURNAL_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String CONTACTS_CREATE =
            "CREATE TABLE if not exists " + CONTACTS_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String CALENDAR_CREATE =
            "CREATE TABLE if not exists " + CALENDAR_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String CONVERSATION_ACTION_SETTINGS_CREATE =
            "CREATE TABLE if not exists " + CONVERSATION_ACTION_SETTINGS_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String QUICK_STEP_SETTINGS_CREATE =
            "CREATE TABLE if not exists " + QUICK_STEP_SETTINGS_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String RSS_FEEDS_CREATE =
            "CREATE TABLE if not exists " + RSS_FEEDS_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";




    //LOG TABLES CREATION STRINGS
    private static final String DELETED_EMAIL_TABLE_CREATE =
            "CREATE TABLE if not exists " + DELETED_EMAIL_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String DELETED_FOLDER_TABLE_CREATE =
            "CREATE TABLE if not exists " + DELETED_FOLDER_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String COPIED_EMAIL_TABLE_CREATE =
            "CREATE TABLE if not exists " + COPIED_EMAIL_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String COPIED_FOLDER_TABLE_CREATE =
            "CREATE TABLE if not exists " + COPIED_FOLDER_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String MOVED_EMAIL_TABLE_CREATE =
            "CREATE TABLE if not exists " + MOVED_EMAIL_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String MOVED_FOLDER_TABLE_CREATE =
            "CREATE TABLE if not exists " + MOVED_FOLDER_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String NEW_FOLDER_TABLE_CREATE =
            "CREATE TABLE if not exists " + NEW_FOLDER_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SENDER_NAME + "," +
                    KEY_SENDER_EMAIL + "," +
                    KEY_SENDER_SUBJECT + "," +
                    KEY_SENDER_DATE + "," +
                    KEY_MSG_BODY + "," +
                    KEY_CC + "," +
                    KEY_BCC + "," +
                    KEY_TABLE_NAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";



    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            Log.w(TAG, INBOX_CREATE);
            db.execSQL(INBOX_CREATE);

            Log.w(TAG, DELETED_CREATE);
            db.execSQL(DELETED_CREATE);

            Log.w(TAG, DRAFTS_CREATE);
            db.execSQL(DRAFTS_CREATE);

            Log.w(TAG, OUTBOX_CREATE);
            db.execSQL(OUTBOX_CREATE);

            Log.w(TAG, JUNK_CREATE);
            db.execSQL(JUNK_CREATE);

            Log.w(TAG, NOTES_CREATE);
            db.execSQL(NOTES_CREATE);

            Log.w(TAG, JOURNAL_CREATE);
            db.execSQL(JOURNAL_CREATE);

            Log.w(TAG, CONTACTS_CREATE);
            db.execSQL(CONTACTS_CREATE);

            Log.w(TAG, CALENDAR_CREATE);
            db.execSQL(CALENDAR_CREATE);

            Log.w(TAG, CONVERSATION_ACTION_SETTINGS_CREATE);
            db.execSQL(CONVERSATION_ACTION_SETTINGS_CREATE);

            Log.w(TAG, QUICK_STEP_SETTINGS_CREATE);
            db.execSQL(QUICK_STEP_SETTINGS_CREATE);

            Log.w(TAG, RSS_FEEDS_CREATE);
            db.execSQL(RSS_FEEDS_CREATE);

            Log.w(TAG, DELETED_EMAIL_TABLE_CREATE);
            db.execSQL(DELETED_EMAIL_TABLE_CREATE);

            Log.w(TAG, DELETED_FOLDER_TABLE_CREATE);
            db.execSQL(DELETED_FOLDER_TABLE_CREATE);

            Log.w(TAG, COPIED_EMAIL_TABLE_CREATE);
            db.execSQL(COPIED_EMAIL_TABLE_CREATE);

            Log.w(TAG, COPIED_FOLDER_TABLE_CREATE);
            db.execSQL(COPIED_FOLDER_TABLE_CREATE);

            Log.w(TAG, MOVED_EMAIL_TABLE_CREATE);
            db.execSQL(MOVED_EMAIL_TABLE_CREATE);

            Log.w(TAG, MOVED_FOLDER_TABLE_CREATE);
            db.execSQL(MOVED_FOLDER_TABLE_CREATE);

            Log.w(TAG, NEW_FOLDER_TABLE_CREATE);
            db.execSQL(NEW_FOLDER_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + INBOX_CREATE);
            onCreate(db);
        }
    }

    public DbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    //Delete all
    public boolean delete() {

        int doneDelete = 0;
        doneDelete = mDb.delete(INBOX_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    // Deleting single email
      public void delete(String senderName, String senderSubject, String date, String folderName)
    {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        String email_id = null;
        String sender_Name = null;
        String sender_Email = null;
        String sender_Subject = null;
        String sender_Date = null;
        String msg_Body = null;
        String _CC = null;
        String _BCC = null;
        String table_name = null;

        if(folderName.equalsIgnoreCase("Inbox")){

            Cursor cursor =QueryData("SELECT * FROM "+folderName);
            if(cursor !=null){
                if(cursor.moveToFirst()){
                    do{
                        if(cursor.getString(1).equals(senderName) && cursor.getString(3).equals(senderSubject) && cursor.getString(4).equals(date)){
                            //GETTING ID OF EMAIL TO BE DELETED FROM DATABASE TABLE
                            email_id = cursor.getString(0);
                            sender_Name = cursor.getString(1);
                            sender_Email = cursor.getString(2);
                            sender_Subject = cursor.getString(3);
                            sender_Date = cursor.getString(4);
                            msg_Body = cursor.getString(5);
                            _CC = cursor.getString(6);
                            _BCC = cursor.getString(7);

                            break;
                        }

                    }while(cursor.moveToNext());
                }
            }

            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE _emailID = '" + email_id + "' ;";
            mDb.execSQL(INSERT);

            //String INSERT_II = "INSERT INTO " + DELETED_EMAIL_TABLE + " VALUES('" + email_id + "', '" + sender_Name + "', '" + sender_Email + "', '" + sender_Subject + "', '" + sender_Date + "', '" + msg_Body + "', '" + _CC + "', '" + _BCC + "', '" + folderName + "') ;";
            //mDb.execSQL(INSERT_II);

            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + folderName + " WHERE _emailID = '" + email_id + "' ;";
            mDb.execSQL(DELETE);
            return;
        }


        if(folderName.equalsIgnoreCase("Drafts")){
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + folderName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Outbox")){
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + folderName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Junk E-mail")){
            String tableName = "Junk_E_mail";
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + tableName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Notes")){
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + folderName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Journal")){
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + folderName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Contacts")){
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + folderName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Calendar")){
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + folderName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Conversation Action Settings")){
            String tableName = "Conversation_Action_Settings";
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + tableName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Quick Step Settings")){
            String tableName = "Quick_Step_Settings";
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + tableName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("RSS Feeds")){
            String tableName = "RSS_Feeds";
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + tableName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        mDb.close();


    }

    // Deleting single email
    public void move(String senderName, String senderSubject, String date, String folderName, String toFolder)
    {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        String email_id = null;
        String sender_Name = null;
        String sender_Email = null;
        String sender_Subject = null;
        String sender_Date = null;
        String msg_Body = null;
        String _CC = null;
        String _BCC = null;


        if(folderName.equalsIgnoreCase("Inbox")){

            Cursor cursor =QueryData("SELECT * FROM "+folderName);
            if(cursor !=null){
                if(cursor.moveToFirst()){
                    do{
                        if(cursor.getString(1).equals(senderName) && cursor.getString(3).equals(senderSubject) && cursor.getString(4).equals(date)){
                            //GETTING ID OF EMAIL TO BE DELETED FROM DATABASE TABLE
                            email_id = cursor.getString(0);
                            sender_Name = cursor.getString(1);
                            sender_Email = cursor.getString(2);
                            sender_Subject = cursor.getString(3);
                            sender_Date = cursor.getString(4);
                            msg_Body = cursor.getString(5);
                            _CC = cursor.getString(6);
                            _BCC = cursor.getString(7);

                            break;
                        }

                    }while(cursor.moveToNext());
                }
            }

            String INSERT = "INSERT INTO " + toFolder + " SELECT * FROM "+folderName+" WHERE _emailID = '" + email_id + "' ;";
            mDb.execSQL(INSERT);

            //String INSERT_II = "INSERT INTO " + toFolder + " VALUES('" + email_id + "', '" + sender_Name + "', '" + sender_Email + "', '" + sender_Subject + "', '" + sender_Date + "', '" + msg_Body + "', '" + _CC + "', '" + _BCC + "', '" + folderName + "') ;";
            //mDb.execSQL(INSERT_II);

            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + folderName + " WHERE _emailID = '" + email_id + "' ;";
            mDb.execSQL(DELETE);
            return;
        }


        if(folderName.equalsIgnoreCase("Drafts")){
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + folderName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Outbox")){
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + folderName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Junk E-mail")){
            String tableName = "Junk_E_mail";
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + tableName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Notes")){
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + folderName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Journal")){
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + folderName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Contacts")){
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + folderName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Calendar")){
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + folderName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Conversation Action Settings")){
            String tableName = "Conversation_Action_Settings";
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + tableName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Quick Step Settings")){
            String tableName = "Quick_Step_Settings";
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + tableName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("RSS Feeds")){
            String tableName = "RSS_Feeds";
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + tableName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        mDb.close();


    }

    // Deleting single email
    public void copy(String senderName, String senderSubject, String date, String folderName, String toFolder)
    {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        String email_id = null;
        String sender_Name = null;
        String sender_Email = null;
        String sender_Subject = null;
        String sender_Date = null;
        String msg_Body = null;
        String _CC = null;
        String _BCC = null;


        if(folderName.equalsIgnoreCase("Inbox")){

            Cursor cursor =QueryData("SELECT * FROM "+folderName);
            if(cursor !=null){
                if(cursor.moveToFirst()){
                    do{
                        if(cursor.getString(1).equals(senderName) && cursor.getString(3).equals(senderSubject) && cursor.getString(4).equals(date)){
                            //GETTING ID OF EMAIL TO BE DELETED FROM DATABASE TABLE
                            email_id = cursor.getString(0);
                            sender_Name = cursor.getString(1);
                            sender_Email = cursor.getString(2);
                            sender_Subject = cursor.getString(3);
                            sender_Date = cursor.getString(4);
                            msg_Body = cursor.getString(5);
                            _CC = cursor.getString(6);
                            _BCC = cursor.getString(7);

                            break;
                        }

                    }while(cursor.moveToNext());
                }
            }

            String INSERT = "INSERT INTO " + toFolder + " SELECT * FROM "+folderName+" WHERE _emailID = '" + email_id + "' ;";
            mDb.execSQL(INSERT);

            //String INSERT_II = "INSERT INTO " + toFolder + " VALUES('" + email_id + "', '" + sender_Name + "', '" + sender_Email + "', '" + sender_Subject + "', '" + sender_Date + "', '" + msg_Body + "', '" + _CC + "', '" + _BCC + "', '" + folderName + "') ;";
            //mDb.execSQL(INSERT_II);

            return;
        }


        if(folderName.equalsIgnoreCase("Drafts")){
            Cursor cursor =QueryData("SELECT * FROM "+folderName);
            if(cursor !=null){
                if(cursor.moveToFirst()){
                    do{
                        if(cursor.getString(1).equals(senderName) && cursor.getString(3).equals(senderSubject) && cursor.getString(4).equals(date)){
                            //GETTING ID OF EMAIL TO BE DELETED FROM DATABASE TABLE
                            email_id = cursor.getString(0);
                            sender_Name = cursor.getString(1);
                            sender_Email = cursor.getString(2);
                            sender_Subject = cursor.getString(3);
                            sender_Date = cursor.getString(4);
                            msg_Body = cursor.getString(5);
                            _CC = cursor.getString(6);
                            _BCC = cursor.getString(7);

                            break;
                        }

                    }while(cursor.moveToNext());
                }
            }

            String INSERT = "INSERT INTO " + toFolder + " SELECT * FROM "+folderName+" WHERE _emailID = '" + email_id + "' ;";
            mDb.execSQL(INSERT);

            //String INSERT_II = "INSERT INTO " + toFolder + " VALUES('" + email_id + "', '" + sender_Name + "', '" + sender_Email + "', '" + sender_Subject + "', '" + sender_Date + "', '" + msg_Body + "', '" + _CC + "', '" + _BCC + "', '" + folderName + "') ;";
            //mDb.execSQL(INSERT_II);

            return;
        }

        if(folderName.equalsIgnoreCase("Outbox")){
            Cursor cursor =QueryData("SELECT * FROM "+folderName);
            if(cursor !=null){
                if(cursor.moveToFirst()){
                    do{
                        if(cursor.getString(1).equals(senderName) && cursor.getString(3).equals(senderSubject) && cursor.getString(4).equals(date)){
                            //GETTING ID OF EMAIL TO BE DELETED FROM DATABASE TABLE
                            email_id = cursor.getString(0);
                            sender_Name = cursor.getString(1);
                            sender_Email = cursor.getString(2);
                            sender_Subject = cursor.getString(3);
                            sender_Date = cursor.getString(4);
                            msg_Body = cursor.getString(5);
                            _CC = cursor.getString(6);
                            _BCC = cursor.getString(7);

                            break;
                        }

                    }while(cursor.moveToNext());
                }
            }

            String INSERT = "INSERT INTO " + toFolder + " SELECT * FROM "+folderName+" WHERE _emailID = '" + email_id + "' ;";
            mDb.execSQL(INSERT);

            //String INSERT_II = "INSERT INTO " + toFolder + " VALUES('" + email_id + "', '" + sender_Name + "', '" + sender_Email + "', '" + sender_Subject + "', '" + sender_Date + "', '" + msg_Body + "', '" + _CC + "', '" + _BCC + "', '" + folderName + "') ;";
            //mDb.execSQL(INSERT_II);

            return;
        }

        if(folderName.equalsIgnoreCase("Junk E-mail")){
            String tableName = "Junk_E_mail";

            Cursor cursor =QueryData("SELECT * FROM "+tableName);
            if(cursor !=null){
                if(cursor.moveToFirst()){
                    do{
                        if(cursor.getString(1).equals(senderName) && cursor.getString(3).equals(senderSubject) && cursor.getString(4).equals(date)){
                            //GETTING ID OF EMAIL TO BE DELETED FROM DATABASE TABLE
                            email_id = cursor.getString(0);
                            sender_Name = cursor.getString(1);
                            sender_Email = cursor.getString(2);
                            sender_Subject = cursor.getString(3);
                            sender_Date = cursor.getString(4);
                            msg_Body = cursor.getString(5);
                            _CC = cursor.getString(6);
                            _BCC = cursor.getString(7);

                            break;
                        }

                    }while(cursor.moveToNext());
                }
            }

            String INSERT = "INSERT INTO " + toFolder + " SELECT * FROM "+tableName+" WHERE _emailID = '" + email_id + "' ;";
            mDb.execSQL(INSERT);

            //String INSERT_II = "INSERT INTO " + toFolder + " VALUES('" + email_id + "', '" + sender_Name + "', '" + sender_Email + "', '" + sender_Subject + "', '" + sender_Date + "', '" + msg_Body + "', '" + _CC + "', '" + _BCC + "', '" + folderName + "') ;";
            //mDb.execSQL(INSERT_II);

            return;
        }

        if(folderName.equalsIgnoreCase("Notes")){
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + folderName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Journal")){
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + folderName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Contacts")){
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + folderName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Calendar")){
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + folderName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Conversation Action Settings")){
            String tableName = "Conversation_Action_Settings";
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + tableName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("Quick Step Settings")){
            String tableName = "Quick_Step_Settings";
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + tableName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        if(folderName.equalsIgnoreCase("RSS Feeds")){
            String tableName = "RSS_Feeds";
            String INSERT = "INSERT INTO " + DELETED_TABLE + " SELECT * FROM "+folderName+" WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(INSERT);
            //mDb.delete(folderName, " senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ", null);
            String DELETE = "DELETE FROM " + tableName + " WHERE senderName = '" + senderName + "' AND senderSubject = '" + senderSubject + "' AND senderDate = '" + date + "' ;";
            mDb.execSQL(DELETE);
            return;
        }

        mDb.close();


    }


    // Deleting folder
    public void deleteRF(String folderName)
    {
        // mDb.delete(SQLITE_TABLE, " folderName = " + name, null);
        //mDb.delete(INBOX_TABLE, " senderName = '" + senderName + "'", null);
        mDb.execSQL("DROP TABLE IF EXISTS " + folderName);

    }

    public Cursor fetchByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(INBOX_TABLE, new String[] {KEY_ROWID,
                            KEY_SENDER_NAME, KEY_SENDER_EMAIL, KEY_SENDER_SUBJECT, KEY_SENDER_DATE, KEY_MSG_BODY, KEY_CC, KEY_BCC},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, INBOX_TABLE, new String[] {KEY_ROWID,
                            KEY_SENDER_NAME, KEY_SENDER_EMAIL, KEY_SENDER_SUBJECT, KEY_SENDER_DATE, KEY_MSG_BODY, KEY_CC, KEY_BCC},
                    KEY_SENDER_NAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAll() {

        Cursor mCursor = mDb.query(INBOX_TABLE, new String[] {KEY_ROWID,
                        KEY_SENDER_NAME, KEY_SENDER_EMAIL, KEY_SENDER_SUBJECT, KEY_SENDER_DATE, KEY_MSG_BODY, KEY_CC, KEY_BCC},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor QueryData(String query){
        return mDb.rawQuery(query, null);
    }

    public void createTable(String tableName) {
        //final SQLiteDatabase db = getWritableDatabase();
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();

        String CREATE_TABLE_NEW_USER =
                "CREATE TABLE if not exists " + tableName + " (" +
                        KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                        KEY_SENDER_NAME + "," +
                        KEY_SENDER_EMAIL + "," +
                        KEY_SENDER_SUBJECT + "," +
                        KEY_SENDER_DATE + "," +
                        KEY_MSG_BODY + "," +
                        KEY_CC + "," +
                        KEY_BCC + "," +
                        KEY_TABLE_NAME + "," +
                        " UNIQUE (" + KEY_ROWID +"));";

        mDb.execSQL(CREATE_TABLE_NEW_USER);

        mDb.close();
    }



    //insert Method for Javalibpst
    public void insertFromPST(){


        Cursor cursor = QueryData("SELECT * FROM "+INBOX_TABLE);
        if(cursor.getCount() > 0){
            return;
        }
        else {


            //code for javalib pst
            try {

                //PSTFile pstFile = new PSTFile("data/data/com.example.kamrankhan.pstapplication/files/kamideegreat@gmail.com.pst");

                //Copying pst file placed in Assets to "data/data/com.example.kamrankhan.pstapplication/files/"
                InputStream is = mCtx.getAssets().open("kamideegreat@gmail.com.pst");  //Openening file from Assets

                //FileName = "kamideegreat@gmail.com.pst";
                //FileLocation = "data/data/com.example.kamrankhan.pstapplication/files";
                String outFileName = FileLocation+"/"+FileName;  //Saving location of file after openeing/reading
                OutputStream os = new FileOutputStream(outFileName); //Saving file
                byte[] buff = new byte[1024];
                int length = 0;
                while((length = is.read(buff) )> 0){
                    os.write(buff,0, length);

                }
                os.flush();
                os.close();
                Log.w("DbAdapter", "PST file copied");

                //Getting file from "data/data/com.example.kamrankhan.pstapplication/files/"
                String path = mCtx.getFileStreamPath("kamideegreat@gmail.com.pst").getAbsolutePath();
                //PSTFile pstFile = new PSTFile("data/data/com.example.kamrankhan.pstapplication/kamideegreat@gmail.com.pst");
                PSTFile pstFile = new PSTFile(path);


                // for getting the root folder of MS Outlook
                PSTFolder pstFolder = pstFile.getRootFolder();

                Vector<PSTFolder> folder = new Vector<PSTFolder>();

                // this will return all the subfolder under the root folder
                folder = pstFolder.getSubFolders();

                // this loop is used for processing all the folders of MS Outlook
                for(int i=0;i<pstFolder.getSubFolderCount();i++)
                {
                    // for displaying the folder name (Main RootFolder i.e Top of Outook data file")
                    //System.out.println("Display Folder Name->"+folder.get(i).getDisplayName());

                    Vector<PSTFolder> personalFolder = new Vector<PSTFolder>();

                    // for getting the folder inside root folder
                    personalFolder = folder.get(i).getSubFolders();

                    // this loop is used for processing all the folders under root folder
                    for(int j=0;j<personalFolder.size();j++)
                    {
                        PSTFolder currentFolder=personalFolder.get(j);

                        String folderName = currentFolder.getDisplayName();
                        String folderCount = String.valueOf(currentFolder.getContentCount());
                        long folderId = currentFolder.getDescriptorNodeId();


                        // display the name of the root folder i.e Inbox, Deleted Items etc
                        //System.out.println(folderName+" ("+folderCount+")");

                        //ADDING VALUES IN DATABASE
                        //if(folderName.equals("Inbox") || folderName.equals("Deleted Items") || folderName.equals("Drafts") || folderName.equals("Junk E-mail") || folderName.equals("Outbox") || folderName.equals("Contacts") || folderName.equals("Notes") || folderName.equals("Tasks")){
                        //create(folderId, folderName, " ("+folderCount+")");
                        //}


                        // this condition is used for checking the name of the folder
                        // if folder name is Inbox then it will enter in IF body
                        //This nested if condition is used to get emails inside root folders

                        if(personalFolder.get(j).getDisplayName().equalsIgnoreCase(folderName))
                        {
                            // for checking inbox folder has email or not
                            if(personalFolder.get(j).getContentCount() > 0)
                            {
                                // for getting the email message
                                PSTMessage email = (PSTMessage)personalFolder.get(j).getNextChild();

                                // if email is not null then it will enter into the loop
                                while (email != null)
                                {

                                    long emailID = email.getDescriptorNodeId();  // for getting the email descriptor id which uniquely identify the email
                                    String senderName=email.getSenderName();
                                    String senderEmail=email.getSenderEmailAddress();
                                    String senderSubject = email.getSubject();

                                    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy - hh:mm");
                                    String date = DATE_FORMAT.format(email.getMessageDeliveryTime());
                                    String msgBody=email.getBodyHTML();
                                    String CC = email.getDisplayCC();
                                    String BCC = email.getDisplayBCC();

                                    //Adding sender name, subject and time of delivery to List
                                    //db.addChildFolder(new ChildFolders(emailID, senderName, senderSubject, date, msgBody));

                                    //mChildFoldersList.add(new ChildFolders(emailID, senderName, senderSubject, date, msgBody));


                                    //Collections.sort((List<Comparable>) db);      //Sorting in ascending
                                    //Collections.reverse(mChildFoldersList);   //Sorting in descending by reversing

                                    if(folderName.equalsIgnoreCase("Inbox")){
                                        ContentValues initialValues = new ContentValues();
                                        initialValues.put(KEY_ROWID, emailID);
                                        initialValues.put(KEY_SENDER_NAME, senderName);
                                        initialValues.put(KEY_SENDER_EMAIL, senderEmail);
                                        initialValues.put(KEY_SENDER_SUBJECT, senderSubject);
                                        initialValues.put(KEY_SENDER_DATE, date);
                                        initialValues.put(KEY_MSG_BODY, msgBody);
                                        initialValues.put(KEY_CC, CC);
                                        initialValues.put(KEY_BCC, BCC);

                                        mDb.insert(INBOX_TABLE, null, initialValues);


                                    }

                                    if(folderName.equalsIgnoreCase("Deleted Items")){
                                        ContentValues initialValues = new ContentValues();
                                        initialValues.put(KEY_ROWID, emailID);
                                        initialValues.put(KEY_SENDER_NAME, senderName);
                                        initialValues.put(KEY_SENDER_EMAIL, senderEmail);
                                        initialValues.put(KEY_SENDER_SUBJECT, senderSubject);
                                        initialValues.put(KEY_SENDER_DATE, date);
                                        initialValues.put(KEY_MSG_BODY, msgBody);
                                        initialValues.put(KEY_CC, CC);
                                        initialValues.put(KEY_BCC, BCC);

                                        mDb.insert(DELETED_TABLE, null, initialValues);

                                    }

                                    if(folderName.equalsIgnoreCase("Drafts")){
                                        ContentValues initialValues = new ContentValues();
                                        initialValues.put(KEY_ROWID, emailID);
                                        initialValues.put(KEY_SENDER_NAME, senderName);
                                        initialValues.put(KEY_SENDER_EMAIL, senderEmail);
                                        initialValues.put(KEY_SENDER_SUBJECT, senderSubject);
                                        initialValues.put(KEY_SENDER_DATE, date);
                                        initialValues.put(KEY_MSG_BODY, msgBody);
                                        initialValues.put(KEY_CC, CC);
                                        initialValues.put(KEY_BCC, BCC);

                                        mDb.insert(DRAFTS_TABLE, null, initialValues);

                                    }

                                    if(folderName.equalsIgnoreCase("Outbox")){
                                        ContentValues initialValues = new ContentValues();
                                        initialValues.put(KEY_ROWID, emailID);
                                        initialValues.put(KEY_SENDER_NAME, senderName);
                                        initialValues.put(KEY_SENDER_EMAIL, senderEmail);
                                        initialValues.put(KEY_SENDER_SUBJECT, senderSubject);
                                        initialValues.put(KEY_SENDER_DATE, date);
                                        initialValues.put(KEY_MSG_BODY, msgBody);
                                        initialValues.put(KEY_CC, CC);
                                        initialValues.put(KEY_BCC, BCC);

                                        mDb.insert(OUTBOX_TABLE, null, initialValues);
                                    }

                                    if(folderName.equalsIgnoreCase("Junk E-mail")){
                                        ContentValues initialValues = new ContentValues();
                                        initialValues.put(KEY_ROWID, emailID);
                                        initialValues.put(KEY_SENDER_NAME, senderName);
                                        initialValues.put(KEY_SENDER_EMAIL, senderEmail);
                                        initialValues.put(KEY_SENDER_SUBJECT, senderSubject);
                                        initialValues.put(KEY_SENDER_DATE, date);
                                        initialValues.put(KEY_MSG_BODY, msgBody);
                                        initialValues.put(KEY_CC, CC);
                                        initialValues.put(KEY_BCC, BCC);

                                        mDb.insert(JUNK_TABLE, null, initialValues);

                                    }

                                    if(folderName.equalsIgnoreCase("Notes")){
                                        ContentValues initialValues = new ContentValues();
                                        initialValues.put(KEY_ROWID, emailID);
                                        initialValues.put(KEY_SENDER_NAME, senderName);
                                        initialValues.put(KEY_SENDER_EMAIL, senderEmail);
                                        initialValues.put(KEY_SENDER_SUBJECT, senderSubject);
                                        initialValues.put(KEY_SENDER_DATE, date);
                                        initialValues.put(KEY_MSG_BODY, msgBody);
                                        initialValues.put(KEY_CC, CC);
                                        initialValues.put(KEY_BCC, BCC);

                                        mDb.insert(NOTES_TABLE, null, initialValues);

                                    }

                                    if(folderName.equalsIgnoreCase("Journal")){
                                        ContentValues initialValues = new ContentValues();
                                        initialValues.put(KEY_ROWID, emailID);
                                        initialValues.put(KEY_SENDER_NAME, senderName);
                                        initialValues.put(KEY_SENDER_EMAIL, senderEmail);
                                        initialValues.put(KEY_SENDER_SUBJECT, senderSubject);
                                        initialValues.put(KEY_SENDER_DATE, date);
                                        initialValues.put(KEY_MSG_BODY, msgBody);
                                        initialValues.put(KEY_CC, CC);
                                        initialValues.put(KEY_BCC, BCC);

                                        mDb.insert(JOURNAL_TABLE, null, initialValues);
                                    }

                                    if(folderName.equalsIgnoreCase("Contacts")){
                                        ContentValues initialValues = new ContentValues();
                                        initialValues.put(KEY_ROWID, emailID);
                                        initialValues.put(KEY_SENDER_NAME, senderName);
                                        initialValues.put(KEY_SENDER_EMAIL, senderEmail);
                                        initialValues.put(KEY_SENDER_SUBJECT, senderSubject);
                                        initialValues.put(KEY_SENDER_DATE, date);
                                        initialValues.put(KEY_MSG_BODY, msgBody);
                                        initialValues.put(KEY_CC, CC);
                                        initialValues.put(KEY_BCC, BCC);


                                        mDb.insert(CONTACTS_TABLE, null, initialValues);
                                    }

                                    if(folderName.equalsIgnoreCase("Calendar")){
                                        ContentValues initialValues = new ContentValues();
                                        initialValues.put(KEY_ROWID, emailID);
                                        initialValues.put(KEY_SENDER_NAME, senderName);
                                        initialValues.put(KEY_SENDER_EMAIL, senderEmail);
                                        initialValues.put(KEY_SENDER_SUBJECT, senderSubject);
                                        initialValues.put(KEY_SENDER_DATE, date);
                                        initialValues.put(KEY_MSG_BODY, msgBody);
                                        initialValues.put(KEY_CC, CC);
                                        initialValues.put(KEY_BCC, BCC);


                                        mDb.insert(CALENDAR_TABLE, null, initialValues);
                                    }

                                    if(folderName.equalsIgnoreCase("Conversation Action Settings")){
                                        ContentValues initialValues = new ContentValues();
                                        initialValues.put(KEY_ROWID, emailID);
                                        initialValues.put(KEY_SENDER_NAME, senderName);
                                        initialValues.put(KEY_SENDER_EMAIL, senderEmail);
                                        initialValues.put(KEY_SENDER_SUBJECT, senderSubject);
                                        initialValues.put(KEY_SENDER_DATE, date);
                                        initialValues.put(KEY_MSG_BODY, msgBody);
                                        initialValues.put(KEY_CC, CC);
                                        initialValues.put(KEY_BCC, BCC);


                                        mDb.insert(CONVERSATION_ACTION_SETTINGS_TABLE, null, initialValues);
                                    }

                                    if(folderName.equalsIgnoreCase("Quick Step Settings")){
                                        ContentValues initialValues = new ContentValues();
                                        initialValues.put(KEY_ROWID, emailID);
                                        initialValues.put(KEY_SENDER_NAME, senderName);
                                        initialValues.put(KEY_SENDER_EMAIL, senderEmail);
                                        initialValues.put(KEY_SENDER_SUBJECT, senderSubject);
                                        initialValues.put(KEY_SENDER_DATE, date);
                                        initialValues.put(KEY_MSG_BODY, msgBody);
                                        initialValues.put(KEY_CC, CC);
                                        initialValues.put(KEY_BCC, BCC);


                                        mDb.insert(QUICK_STEP_SETTINGS_TABLE, null, initialValues);
                                    }

                                    if(folderName.equalsIgnoreCase("RSS Feeds")){
                                        ContentValues initialValues = new ContentValues();
                                        initialValues.put(KEY_ROWID, emailID);
                                        initialValues.put(KEY_SENDER_NAME, senderName);
                                        initialValues.put(KEY_SENDER_EMAIL, senderEmail);
                                        initialValues.put(KEY_SENDER_SUBJECT, senderSubject);
                                        initialValues.put(KEY_SENDER_DATE, date);
                                        initialValues.put(KEY_MSG_BODY, msgBody);
                                        initialValues.put(KEY_CC, CC);
                                        initialValues.put(KEY_BCC, BCC);


                                        mDb.insert(RSS_FEEDS_TABLE, null, initialValues);
                                    }


                                    email = (PSTMessage)personalFolder.get(j).getNextChild();
                                }


                            }
                        }
                        else
                        {
                            continue;
                        }


                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (PSTException e) {
                e.printStackTrace();
            }

        }


    }



}
