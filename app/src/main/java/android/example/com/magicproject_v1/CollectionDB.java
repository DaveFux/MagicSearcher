package android.example.com.magicproject_v1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.example.com.magicproject_v1.classes.Card;
import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;

public class CollectionDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "COLLECTIONS.BD";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_CARDS = "t_cards";
    //public static final String TABLE_COLLECTIONS = "t_collection";
    public static final String TABLE_COLLECTIONS = "t_collections";

    //public static final String TABLE_DECKS = "t_decks";
    public static final String COL_ID = "col_id";
    public static final String COL_NOME = "col_nome";
    /*
    public static final String COL_TYPE = "col_type";
    public static final String COL_MANACOST = "col_manaCost";
    public static final String COL_FLAVORTEXT = "col_flavorText";
    public static final String COL_ORACLETEXT = "col_oracleText";
    public static final String COL_EXPANSIONNAME = "col_expansionName";
    public static final String COL_RARITY = "col_rarity";
    public static final String COL_POWER = "col_power";
    public static final String COL_TOUGHNESS = "col_toughness";
    */
    public static final String TAG = "@CollectionDB";


    public CollectionDB(Context context){
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public CollectionDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        installDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        boolean bThereIsDatabaseUpgrade = newVersion > oldVersion;
        if(bThereIsDatabaseUpgrade){
            uninstallDB(db);
            installDB(db);
        }
    }

    private void uninstallDB(SQLiteDatabase db) {
        Objects.requireNonNull(db);
        String strSQL = statementForTableCollectionsDestruction();
        try {
            db.execSQL(strSQL);
        }catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void installDB(SQLiteDatabase db) {
        Objects.requireNonNull(db);
        String strSQL = statementForTableCollectionsDestruction();
        try {
            db.execSQL(strSQL);
        }catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public long addCard (Card card){
        SQLiteDatabase dbw = this.getWritableDatabase();
        if (dbw!=null) {
            ContentValues cv = new ContentValues();
            cv.put(COL_NOME, card.getName());

            long id = dbw.insert(TABLE_CARDS, null, cv);
            dbw.close();
            return id;
        }
        return -2;
    }

    public long addCardToDeck (String deckName, Card card, int quantity){
        //TODO:
        return -2;
    }

    public long removeCardFromDeck (String deckName, Card card, int quantity){
        //TODO:
        return -2;
    }

    public ArrayList<String> retrieveAllFromDeck (String deckName){
        //TODO:
        return null;
    }

    public ArrayList<String> retrieveAll(){
        return retrieveAll("", "");
    }

    public ArrayList<String> retrieveAll(String filter){
        return retrieveAll(COL_NOME, filter);
    }

    public ArrayList<String> retrieveAll(String columnName, String filter){
        ArrayList<String> retorno = new ArrayList<>();
        SQLiteDatabase dbr = this.getReadableDatabase();
        if (dbr!=null) {
            String query = "select * from " + TABLE_COLLECTIONS;
            if(!columnName.equals("")) {
                query += " where " + columnName + " like '%" + filter + "%'";
            }
            Cursor cursor = dbr.rawQuery(query, null);
            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()) {
                    retorno.add(cursor.getString(1));
                    cursor.moveToNext();
                }
            }
            dbr.close();
        }
        return retorno;
    }

    public int count(String tableName){
        return retrieveAll().size();
    }

    public void clear(){
        SQLiteDatabase dbw = this.getWritableDatabase();
        uninstallDB(dbw);
        installDB(dbw);
    }

    private String statementForCollectionsCreation(){
        String strRet;
        //id,nome
        strRet = String.format("CREATE TABLE IF NOT EXISTS %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , %s TEXT NOT NULL",
                TABLE_COLLECTIONS, COL_ID, COL_NOME);
        return strRet;
    }

    private String statementForTableCollectionsDestruction(){
        return "DROP TABLE IF EXISTS " + TABLE_COLLECTIONS;
    }
}
