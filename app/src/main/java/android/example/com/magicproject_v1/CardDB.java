package android.example.com.magicproject_v1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.example.com.magicproject_v1.classes.Card;
import android.util.Log;

import java.util.Objects;

import static android.example.com.magicproject_v1.ExampleDB.TABLE_MESSAGES;

public class CardDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CARDS.BD";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_CARDS = "t_cards";
    public static final String TABLE_COLLECTION = "t_collection";
    public static final String TABLE_DECKS = "t_decks";
    public static final String COL_ID = "col_id";
    public static final String COL_NOME = "col_nome";
    public static final String COL_TYPE = "col_type";
    public static final String COL_MANACOST = "col_manaCost";
    public static final String COL_FLAVORTEXT = "col_flavorText";
    public static final String COL_ORACLETEXT = "col_oracleText";
    public static final String COL_EXPANSIONNAME = "col_expansionName";
    public static final String COL_RARITY = "col_rarity";
    public static final String COL_POWER = "col_power";
    public static final String COL_TOUGHNESS = "col_toughness";
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
        String strSQL = statementForTableCardsDestruction();
        try {
            db.execSQL(strSQL);
        }catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void installDB(SQLiteDatabase db) {
        Objects.requireNonNull(db);
        String strSQL = statementForTableCardsCreation();
        try {
            db.execSQL(strSQL);
        }catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private String statementForTableCardsCreation(){
        String strRet;

        strRet = String.format("CREATE TABLE IF NOT EXISTS %s " +
                "(%s INTEGER NOT NULL AUTOINCREMENT , %s TEXT NOT NULL," +
                " %s TEXT NOT NULL, %s TEXT, %s TEXT, %s TEXT," +
                " %s TEXT NOT NULL, %s TEXT NOT NULL, %s INTEGER, %s INTEGER)",
                TABLE_CARDS, COL_ID, COL_NOME, COL_TYPE, COL_MANACOST, COL_FLAVORTEXT,
                COL_ORACLETEXT, COL_EXPANSIONNAME, COL_RARITY, COL_POWER, COL_TOUGHNESS);
        return strRet;
    }

    public long addMessage (
            Card card
    ){
        SQLiteDatabase objectThatWillAllowMeToWriteToTheDatabase =
                this.getWritableDatabase();

        if (objectThatWillAllowMeToWriteToTheDatabase!=null)
        {
            ContentValues cv = new ContentValues();
            cv.put(COL_NOME, card.getName());


            long iWhereInsertedOrMinus1OnFailure =
                    objectThatWillAllowMeToWriteToTheDatabase.insert(
                            TABLE_CARDS,
                            null, //don't want to provide the name of a null column
                            cv
                    );
            objectThatWillAllowMeToWriteToTheDatabase.close();
            return iWhereInsertedOrMinus1OnFailure;
        }//if
        //return -2;
        return -2;
    }

    private String statementForTableCardsDestruction(){
        return "DROP TABLE IF EXISTS " + TABLE_CARDS;
    }

    private String statementForTableCollectionDestruction(){
        return "DROP TABLE IF EXISTS " + TABLE_COLLECTION;
    }

    private String statementForTableDecksDestruction(){
        return "DROP TABLE IF EXISTS " + TABLE_DECKS;
    }
}
