package hanaalalwi.bookapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import hanaalalwi.bookapp.data.BookContract.BookEntryClass;

/**
 * Created by HanaAlalwi on 1/23/2018.
 */

public class BookSQLHelper extends SQLiteOpenHelper {

    private static final String DB_name = "Book.db";
    private static final int version = 1;

    public BookSQLHelper(Context context) {
        super(context, DB_name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_BOOK_TABLE = "CREATE TABLE " + BookEntryClass.TABLE_NAME + " ("
                + BookEntryClass._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntryClass.PRODUCT_NAME + " TEXT NOT NULL, "
                + BookEntryClass.PRICE + " INTEGER NOT NULL, "
                + BookEntryClass.QUANTITY + " INTEGER NOT NULL, "
                + BookEntryClass.SUPPLIER_NAME + " TEXT, "
                + BookEntryClass.SUPPLIER_EMAIL + " TEXT, "
                + BookEntryClass.SUPPLIER_PHONE_NUMBER + " LONG );";
        db.execSQL(SQL_CREATE_BOOK_TABLE);
    }

    //this method allow the user to delete all data from the database or add a new column to upgrade it, but until now I don't want to remove the data or modify it
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
