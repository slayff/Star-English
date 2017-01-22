package ru.slayff.starenglish.QuizSystem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;


/**
 * Created by slayff on 09.04.15.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static DataBaseHelper sInstance;

    // Hardcode database path (not recommended to use)
    // private static String DB_PATH ="/data/data/ru.slayff.starenglish/databases";
    private static String DB_PATH;
    public static String DB_NAME = "Les_database.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor is private and only used in static method getInstance
     * @param context
     */
    private DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        DB_PATH = context.getFilesDir().getParentFile().getPath() + "/databases/";
    }

    /**
     * Method to initialize DataBaseHelper with the Singleton pattern
     * @param context
     * @return DataBaseHelper object
     */
    public static DataBaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataBaseHelper(context);
        }
        return sInstance;
    }

    /**
     * Creating new empty database and rewrite it with our own one
     * @throws IOException
     */
    public void createDataBase() throws IOException{
        boolean dbExist = checkDataBase();
        if (dbExist) {
        } //nothing to do - database is already exists
        else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e("AppLog", "Error copying database");
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Checking database in case not to copy it every time the app is launched
     * @return true if exists, false if not
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkedDB = null;
        try {
            String t_Path = DB_PATH + DB_NAME;
            checkedDB = SQLiteDatabase.openDatabase(t_Path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.e("AppLog", "Base doesn't exist yet");
            //base doesn't exist yet
        }
        if (checkedDB != null) {
            checkedDB.close();
        }
        return checkedDB!=null? true:false;
    }

    /**
     * Copying database from assets to system path by copying the stream of bytes
     */
    private void copyDataBase() throws IOException{
        //opening local DB as an inputstream
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        //opening empty DB as an outputstream
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /**
     * Opens DataBase
     * @throws SQLException
     */
    public void openDataBase() throws SQLException {
        String t_Path = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(t_Path, null, SQLiteDatabase.OPEN_READONLY);
    }

    /**
     * Closes DataBase
     */
    @Override
    public synchronized void close() {
        if (myDataBase != null) myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     *
     * @return Cursor with all data about exact lesson
     */
    public Cursor getCursor(int lesson, String qType) {
        String selection = "lesson = "+ lesson;
        return myDataBase.query(qType, null, selection, null, null, null, null);
    }

    /**
     *
     * @param id shows the exact id of data to move Cursor to this position
     * @param qType is used to set the exact type of question (The same as table name in DataBase)
     * @return Curson with only 1 position
     */
    public Cursor getCursorAtId(int id, String qType) {
        String selection = "_id = " + id;
        return myDataBase.query(qType, null, selection, null, null, null, null);
    }
}
