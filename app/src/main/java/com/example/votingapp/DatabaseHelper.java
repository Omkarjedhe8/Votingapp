package com.example.votingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Votings";
    private static final int DATABASE_VERSION = 1;

    // Table and columns
    public static final String TABLE_USERS = "Users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_USER_HAS_VOTED = "has_voted";
    private static final String COLUMN_VOTE_OPTION = "vote_option";


    // Table and columns for votes

    public static final String TABLE_VOTES = "votes";
    public static final String COLUMN_VOTE_ID = "vote_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_CANDIDATE_NAME = "candidate_name";



    // Create table SQL
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_USER_HAS_VOTED + " INTEGER DEFAULT 0, " +
                    COLUMN_VOTE_OPTION + " TEXT)";

    private static final String CREATE_TABLE_VOTES =
            "CREATE TABLE " + TABLE_VOTES + " (" +
                    COLUMN_VOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID + " INTEGER, " +
                    COLUMN_CANDIDATE_NAME + " TEXT)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(CREATE_TABLE_VOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOTES);


        // Create new table schema
        onCreate(db);
        // Add further version upgrades here
      /*  if (oldVersion < 2) {
            // Example for upgrading from version 1 to version 2
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_HAS_VOTED + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_VOTE_OPTION + " TEXT");
        }*/


    }


    public boolean insertUser(String username, String email, String password, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_PHONE, phone);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public void logAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String dbUsername = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                String dbPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
                Log.d("DatabaseHelper", "User: " + dbUsername);
                Log.d("DatabaseHelper", "Password: " + dbPassword);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }


    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1;

        String[] columns = { COLUMN_ID };
        String selection = COLUMN_EMAIL + "=?";
        String[] selectionArgs = { username };

        Log.d("DatabaseHelper", "getUserId - Query: SELECT " + COLUMN_ID + " FROM " + TABLE_USERS +
                " WHERE " + COLUMN_EMAIL + "='" + username + "'");

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        Log.d("DatabaseHelper", "getUserId - email: " + username + ", userId: " + userId);

        return userId;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        boolean exists = cursor.getCount() > 0;

        if (cursor.moveToFirst()) {
            do {
                String dbEmail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                String dbPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
                Log.d("DatabaseHelper", "User: " + dbEmail);
                Log.d("DatabaseHelper", "Password: " + dbPassword);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Log.d("DatabaseHelper", "User Exists: " + exists);
        return exists;
    }

    public boolean hasUserVoted(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { COLUMN_USER_HAS_VOTED };
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        boolean hasVoted = false;
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_USER_HAS_VOTED);
            if (columnIndex != -1) {
                hasVoted = cursor.getInt(columnIndex) == 1;
            } else {
                // Handle case where COLUMN_USER_HAS_VOTED column is not found
                Log.e("DatabaseHelper", "COLUMN_USER_HAS_VOTED column not found in cursor");
            }
            cursor.close();
        } else {
            // Handle case where cursor is null or empty
            Log.e("DatabaseHelper", "No data found for userId: " + userId);
        }
        return hasVoted;
    }


    public boolean recordVote(int userId, String voteOption) {
        // Verify user existence or log the user ID here
        Log.d("DatabaseHelper", "Updating user ID " + userId + " with vote option " + voteOption);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_HAS_VOTED, 1);
        values.put(COLUMN_VOTE_OPTION, voteOption);

        int result = db.update(TABLE_USERS, values, COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
        Log.d("DatabaseHelper", "Update result: " + result);  // Log update result

        db.close();
        return result > 0;
    }

    public int getCandidateVoteCount(String candidateName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_VOTES +
                " WHERE " + COLUMN_CANDIDATE_NAME + " = ?", new String[]{candidateName});
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return count;
    }








}


