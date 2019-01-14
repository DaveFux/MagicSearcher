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
import java.util.List;
import java.util.Objects;

public class CardDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CARDS.BD";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CARDS = "t_cards";
    private static final String TABLE_COLLECTIONS = "t_collections";
    private static final String TABLE_CARDS_IN_COLLECTION = "t_cards_in_collection";
    private static final String COL_ID = "col_id";
    private static final String COL_ID_CARDS = "col_id_cards";
    private static final String COL_ID_COLLECTIONS = "col_id_collections";
    private static final String COL_NAME = "col_name";
    private static final String COL_TYPE = "col_type";
    private static final String COL_MANACOST = "col_manaCost";
    private static final String COL_FLAVORTEXT = "col_flavorText";
    private static final String COL_ORACLETEXT = "col_oracleText";
    private static final String COL_EXPANSIONNAME = "col_expansionName";
    private static final String COL_RARITY = "col_rarity";
    private static final String COL_POWER = "col_power";
    private static final String COL_TOUGHNESS = "col_toughness";
    private static final String COL_IMAGE = "col_image";
    private static final String COL_THUMBNAIL = "col_thumbnail";
    private static final String COL_TAGS = "col_tags";
    private static final String COL_QUANTITY = "col_quantity";
    private static final String TAG = "@CardDB";

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

    // COMPLETED
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

    // COMPLETED
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

    // COMPLETED
    public long addCard (Card card){
        SQLiteDatabase dbw = this.getWritableDatabase();
        if (dbw!=null) {
            ContentValues cv = new ContentValues();
            cv.put(COL_ID, card.getId());
            cv.put(COL_NAME, card.getName());
            cv.put(COL_TYPE, card.getType());
            cv.put(COL_MANACOST, card.getManaCost().toString());
            cv.put(COL_FLAVORTEXT, card.getFlavorText());
            cv.put(COL_ORACLETEXT, card.getOracleText());
            cv.put(COL_EXPANSIONNAME, card.getExpansionName());
            cv.put(COL_RARITY, card.getRarity().toString());
            cv.put(COL_POWER, card.getPower());
            cv.put(COL_TOUGHNESS, card.getToughness());
            cv.put(COL_IMAGE, card.getImage());
            cv.put(COL_THUMBNAIL, card.getThumbnail());
            long id = dbw.insert(TABLE_CARDS, null, cv);
            dbw.close();
            return id;
        }
        return -2;
    }

    // COMPLETED
    public long addCollection(Collection collection){
        SQLiteDatabase dbw = this.getWritableDatabase();
        if (dbw!=null) {
            ContentValues cv = new ContentValues();
            cv.put(COL_NAME, collection.getName());
            cv.put(COL_TAGS, collection.getTags());
            long id = dbw.insert(TABLE_COLLECTIONS, null, cv);
            dbw.close();
            for(Card c : collection.getCards()){
                addCardInCollection(c.getId(), (int) id);
            }
            return id;
        }
        return -2;
    }

    // NOT COMPLETED
    public long editCollection(int id, String name, String tags){
        SQLiteDatabase dbw = this.getWritableDatabase();
        if (dbw!=null) {
            ContentValues cv = new ContentValues();
            cv.put(COL_NAME, name);
            cv.put(COL_TAGS, tags);
            int rows = dbw.update(TABLE_COLLECTIONS, cv, COL_ID + " = " + id, null);
            dbw.close();
            return rows;
        }
        return -2;
    }

    // COMPLETED
    public long addCardInCollection(String c, int collectionID) {
        SQLiteDatabase dbw = this.getWritableDatabase();
        if (dbw!=null) {
            ContentValues cv = new ContentValues();
            cv.put(COL_ID_COLLECTIONS, collectionID);
            cv.put(COL_ID_CARDS, c);
            cv.put(COL_QUANTITY, 1);

            long id = dbw.insert(TABLE_CARDS_IN_COLLECTION, null, cv);
            dbw.close();
            return id;
        }
        return -2;
    }

    // COMPLETED
    public ArrayList<Collection> retrieveAllCollections(){
        return retrieveAllCollections("");
    }

    // COMPLETED
    public ArrayList<Collection> retrieveAllCollections(String filter){
        ArrayList<Collection> retorno = new ArrayList<>();
        SQLiteDatabase dbr = this.getReadableDatabase();
        if (dbr!=null) {
            String query = "select * from " + TABLE_COLLECTIONS + " where " + COL_NAME + " like '%" + filter + "%'";
            Cursor cursor = dbr.rawQuery(query, null);
            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()) {
                    Collection c = new Collection(cursor.getString(1), cursor.getString(2));
                    c.setNumberOfCards(countCardsInCollection(cursor.getInt(0)));
                    retorno.add(c);
                    cursor.moveToNext();
                }
            }
            dbr.close();
            cursor.close();
        }
        return retorno;
    }

    // COMPLETED
    public boolean deleteAllCollections(){
        SQLiteDatabase dbw = this.getWritableDatabase();
        if (dbw!=null) {
            String query = "delete from " + TABLE_COLLECTIONS;
            dbw.execSQL(query);
            dbw.close();
            return true;
        }
        return false;
    }

    // COMPLETED
    private int countCardsInCollection(int collectionId){
        int count = 0;
        SQLiteDatabase dbr = this.getReadableDatabase();
        if (dbr!=null) {
            String query = "select count(*) from " + TABLE_CARDS_IN_COLLECTION
                    + " where " + COL_ID_COLLECTIONS + " = " + collectionId;
            Cursor cursor = dbr.rawQuery(query, null);
            if(cursor.moveToFirst()){
                count = cursor.getInt(0);
            }
            dbr.close();
            cursor.close();
        }
        return count;
    }

    // COMPLETED
    public ArrayList<Card> retrieveAllCardsInCollection(int collectionId){
        ArrayList<Card> retorno = new ArrayList<>();
        SQLiteDatabase dbr = this.getReadableDatabase();
        if (dbr!=null) {
            String query = "select " + COL_ID_CARDS + " from " + TABLE_CARDS_IN_COLLECTION
                    + " where " + COL_ID_COLLECTIONS + " = " + collectionId;
            Cursor cursor = dbr.rawQuery(query, null);
            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()) {
                    String cardId = cursor.getString(0);
                    retorno.add(retrieveCard(cardId));
                    cursor.moveToNext();
                }
            }
            dbr.close();
            cursor.close();
        }
        return retorno;
    }

    // COMPLETED
    public Card retrieveCard(){
        Card retorno = null;
        SQLiteDatabase dbr = this.getReadableDatabase();
        if (dbr!=null) {
            String query = "select * from " + TABLE_CARDS + " order by random() limit 1";
            Cursor cursor = dbr.rawQuery(query, null);
            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()) {
                    retorno = new Card(cursor.getString(0), cursor.getString(1),
                            cursor.getString(2), cursor.getString(3), cursor.getString(4),
                            cursor.getString(5), cursor.getString(6), cursor.getString(7),
                            cursor.getInt(8), cursor.getInt(9), cursor.getString(10),
                            cursor.getString(11));
                    cursor.moveToNext();
                }
            }
            dbr.close();
            cursor.close();
        }
        return retorno;
    }

    // COMPLETED
    private Card retrieveCard(String cardID){
        Card retorno = null;
        SQLiteDatabase dbr = this.getReadableDatabase();
        if (dbr!=null) {
            String query = "select * from " + TABLE_CARDS + " where " +
                    COL_ID + " like '%" + cardID + "%'";
            Cursor cursor = dbr.rawQuery(query, null);
            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()) {
                    retorno = new Card(cursor.getString(0), cursor.getString(1),
                            cursor.getString(2), cursor.getString(3), cursor.getString(4),
                            cursor.getString(5), cursor.getString(6), cursor.getString(7),
                            cursor.getInt(8), cursor.getInt(9), cursor.getString(10),
                            cursor.getString(11));
                    cursor.moveToNext();
                }
            }
            dbr.close();
            cursor.close();
        }
        return retorno;
    }

    // COMPLETED
    public ArrayList<Card> retrieveCards(){
        return retrieveCards("", "");
    }

    // COMPLETED
    public ArrayList<Card> retrieveCards(String filter){
        return retrieveCards(COL_NAME, filter);
    }

    // COMPLETED
    private ArrayList<Card> retrieveCards(String columnName, String filter){
        ArrayList<Card> retorno = new ArrayList<>();
        SQLiteDatabase dbr = this.getReadableDatabase();
        if (dbr!=null) {
            String query = "select * from " + TABLE_CARDS;
            if(columnName.length() > 0) {
                query += " where " + columnName + " like '%" + filter + "%'";
            }
            Cursor cursor = dbr.rawQuery(query, null);
            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()) {
                    Card c = new Card(cursor.getString(0), cursor.getString(1),
                            cursor.getString(2), cursor.getString(3), cursor.getString(4),
                            cursor.getString(5), cursor.getString(6), cursor.getString(7),
                            cursor.getInt(8), cursor.getInt(9), cursor.getString(10),
                            cursor.getString(11));
                    retorno.add(c);
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
                        "(%s TEXT PRIMARY KEY NOT NULL , %s TEXT NOT NULL," +
                        " %s TEXT NOT NULL, %s TEXT, %s TEXT, %s TEXT," +
                        " %s TEXT NOT NULL, %s TEXT NOT NULL, %s INTEGER," +
                        " %s INTEGER, %s TEXT NOT NULL, %s TEXT NOT NULL)",
                TABLE_CARDS, COL_ID, COL_NAME, COL_TYPE, COL_MANACOST, COL_FLAVORTEXT,
                COL_ORACLETEXT, COL_EXPANSIONNAME, COL_RARITY, COL_POWER, COL_TOUGHNESS,
                COL_IMAGE, COL_THUMBNAIL);
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
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        " %s INTEGER NOT NULL, %s TEXT NOT NULL, %s INTEGER NOT NULL)",
                TABLE_CARDS_IN_COLLECTION, COL_ID, COL_ID_COLLECTIONS, COL_ID_CARDS, COL_QUANTITY);
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
