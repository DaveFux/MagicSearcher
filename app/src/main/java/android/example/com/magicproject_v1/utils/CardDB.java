package android.example.com.magicproject_v1.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.example.com.magicproject_v1.classes.Card;
import android.example.com.magicproject_v1.classes.Collection;
import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;

public class CardDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CARDS.BD";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_CARDS = "t_cards";
    public static final String TABLE_COLLECTIONS = "t_collections";
    public static final String TABLE_CARDS_IN_COLLECTION = "t_cards_in_collections";
    public static final String COL_ID = "col_id";
    public static final String COL_ID_CARDS = "col_id_cards";
    public static final String COL_ID_COLLECTIONS = "col_id_collections";
    public static final String COL_NAME = "col_name";
    public static final String COL_TYPE = "col_type";
    public static final String COL_MANACOST = "col_manaCost";
    public static final String COL_FLAVORTEXT = "col_flavorText";
    public static final String COL_ORACLETEXT = "col_oracleText";
    public static final String COL_EXPANSIONNAME = "col_expansionName";
    public static final String COL_RARITY = "col_rarity";
    public static final String COL_POWER = "col_power";
    public static final String COL_TOUGHNESS = "col_toughness";
    public static final String COL_TAGS = "col_tags";
    public static final String COL_QUANTITY = "col_quantity";
    public static final String TAG = "@CardDB";

    public CardDB(Context context){
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public CardDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
        String strSQLTableCardsDestruction = statementForTableCardsDestruction();
        String strSQLTableCollectionsDestruction = statementForTableCollectionDestruction();
        String strSQLTableCardsInCollectionDestruction = statementForTableCardsInCollectionDestruction();
        try {
            db.execSQL(strSQLTableCardsDestruction);
            db.execSQL(strSQLTableCollectionsDestruction);
            db.execSQL(strSQLTableCardsInCollectionDestruction);
        }catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void installDB(SQLiteDatabase db) {
        Objects.requireNonNull(db);
        String strSQLTableCardsCreation = statementForTableCardsCreation();
        String strSQLTableCollectionsCreation = statementForCollectionsCreation();
        String strSQLTableCardsInCollectionCreation = statementForCardsInCollectionsCreation();
        try {
            db.execSQL(strSQLTableCardsCreation);
            db.execSQL(strSQLTableCollectionsCreation);
            db.execSQL(strSQLTableCardsInCollectionCreation);
        }catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public long addCard (Card card){
        SQLiteDatabase dbw = this.getWritableDatabase();
        if (dbw!=null) {
            ContentValues cv = new ContentValues();
            cv.put(COL_NAME, card.getName());
            cv.put(COL_TYPE, card.getToughness());
            cv.put(COL_MANACOST, card.getManaCost().toString());
            cv.put(COL_FLAVORTEXT, card.getFlavorText());
            cv.put(COL_ORACLETEXT, card.getOracleText());
            cv.put(COL_EXPANSIONNAME, card.getExpansionName());
            cv.put(COL_RARITY, card.getRarity().toString());
            cv.put(COL_POWER, card.getPower());
            cv.put(COL_TOUGHNESS, card.getToughness());

            long id = dbw.insert(TABLE_CARDS, null, cv);
            dbw.close();
            return id;
        }
        return -2;
    }

    public long addCollection(Collection collection){
        SQLiteDatabase dbw = this.getWritableDatabase();
        if (dbw!=null) {
            ContentValues cv = new ContentValues();
            cv.put(COL_NAME, collection.getName());
            cv.put(COL_TAGS, collection.getTags());

            long id = dbw.insert(TABLE_COLLECTIONS, null, cv);
            dbw.close();
            return id;
        }
        return -2;
    }

    public ArrayList<Collection> retrieveAllCollections(){
        return retrieveAllCollections("");
    }

    public ArrayList<Collection> retrieveAllCollections(String filter){
        ArrayList<Collection> retorno = new ArrayList<>();
        SQLiteDatabase dbr = this.getReadableDatabase();
        if (dbr!=null) {
            String query = "select * from " + TABLE_COLLECTIONS + " where " + COL_NAME + " like '%" + filter + "%'";
            Cursor cursor = dbr.rawQuery(query, null);
            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()) {
                    Collection c = new Collection(cursor.getString(1), cursor.getString(2), 10);
                    retorno.add(c);
                    cursor.moveToNext();
                }
            }
            dbr.close();
            cursor.close();
        }
        return retorno;
    }

    public ArrayList<String> retrieveAllCards(){
        return retrieveAllCards("", "");
    }

    public ArrayList<String> retrieveAllCards(String filter){
        return retrieveAllCards(COL_NAME, filter);
    }

    public ArrayList<String> retrieveAllCards(String columnName, String filter){
        ArrayList<String> retorno = new ArrayList<>();
        SQLiteDatabase dbr = this.getReadableDatabase();
        if (dbr!=null) {
            String query = "select * from " + TABLE_CARDS;
            if(columnName.length() > 0) {
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
            cursor.close();
        }
        return retorno;
    }

    public void clear(){
        SQLiteDatabase dbw = this.getWritableDatabase();
        uninstallDB(dbw);
        installDB(dbw);
    }

    private String statementForTableCardsCreation(){
        String strRet;

        strRet = String.format("CREATE TABLE IF NOT EXISTS %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , %s TEXT NOT NULL," +
                        " %s TEXT NOT NULL, %s TEXT, %s TEXT, %s TEXT," +
                        " %s TEXT NOT NULL, %s TEXT NOT NULL, %s INTEGER, %s INTEGER)",
                TABLE_CARDS, COL_ID, COL_NAME, COL_TYPE, COL_MANACOST, COL_FLAVORTEXT,
                COL_ORACLETEXT, COL_EXPANSIONNAME, COL_RARITY, COL_POWER, COL_TOUGHNESS);
        return strRet;
    }

    private String statementForCollectionsCreation(){
        String strRet;

        strRet = String.format("CREATE TABLE IF NOT EXISTS %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , %s TEXT NOT NULL, " +
                        "%s TEXT NOT NULL)",
                TABLE_COLLECTIONS, COL_ID, COL_NAME, COL_TAGS);
        return strRet;
    }

    private String statementForCardsInCollectionsCreation(){
        String strRet;

        strRet = String.format("CREATE TABLE IF NOT EXISTS %s " +
                        "(%s INTEGER PRIMARY KEY NOT NULL ," +
                        "%s INTEGER PRIMARY KEY NOT NULL, %s INTEGER NOT NULL)",
                TABLE_COLLECTIONS, COL_ID_COLLECTIONS, COL_ID_CARDS, COL_QUANTITY);
        return strRet;
    }

    private String statementForTableCardsDestruction(){
        return "DROP TABLE IF EXISTS " + TABLE_CARDS;
    }

    private String statementForTableCollectionDestruction(){
        return "DROP TABLE IF EXISTS " + TABLE_COLLECTIONS;
    }

    private String statementForTableCardsInCollectionDestruction(){
        return "DROP TABLE IF EXISTS " + TABLE_CARDS_IN_COLLECTION;
    }

}
