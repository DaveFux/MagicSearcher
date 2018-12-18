package android.example.com.magicproject_v1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Objects;

public class ExampleDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NOMEAQUIBRUDDA.BD";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_MESSAGES = "t_messages";
    public static final String COL_ID = "_id";
    public static final String COL_MESSAGES = "col_msg";
    public static final String TAG = "@MessagesDB";

    /*public static final String SQLITE_CREATE_MESSAGES_TABLE =
            " create table if not exists " + TABLE_MESSAGES + " ("+
            COL_ID + " integer not null autoincrement ,"+
            COL_MESSAGES + " text )";*/

    public ExampleDB(Context context){
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ExampleDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // metodo usado na primeira chamada a db
    @Override
    public void onCreate(SQLiteDatabase db) {
        installDB(db);
    }

    // metodo chamado pelo android quando houver mudancas de versao na base de dados
    // ( na melhor pratica -> backup data, destroy and rebuild )
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        boolean bThereIsDatabaseUpgrade = newVersion > oldVersion;
        if(bThereIsDatabaseUpgrade){
            uninstallDB(db);
            installDB(db);
        }
    }

    public String getSQLiteStatementForTableMessagesCreation(){
        String strRet;

        strRet = String.format("CREATE TABLE IF NOT EXISTS %s " +
                "(%s INTEGER NOT NULL AUTOINCREMENT , %s TEXT )",
                TABLE_MESSAGES, COL_ID, COL_MESSAGES);
        return strRet;
    }

    public String getSQLiteStatementForTableMessagesDestruction(){
        return "DROP TABLE IF EXISTS " + TABLE_MESSAGES;
    }

    private void uninstallDB(SQLiteDatabase db) {
        Objects.requireNonNull(db);
        String strSQL = getSQLiteStatementForTableMessagesDestruction();
        try {
            db.execSQL(strSQL);
        }catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void installDB(SQLiteDatabase db) {
        Objects.requireNonNull(db);
        String strSQL = getSQLiteStatementForTableMessagesCreation();
        try {
            db.execSQL(strSQL);
        }catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public long addMessage(String str){
        SQLiteDatabase dbw = this.getWritableDatabase();
        Objects.requireNonNull(dbw);
        ContentValues cv = new ContentValues();
        cv.put(COL_MESSAGES, str);
        long hermits = dbw.insert(TABLE_MESSAGES, null, cv);
        dbw.close();
        return hermits;
    }
}
