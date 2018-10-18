package hanaalalwi.bookapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by HanaAlalwi on 1/25/2018.
 */

public class BookPovider extends ContentProvider {
    public static final String LOG_TAG = BookPovider.class.getSimpleName();
    private static final int BOOKS = 100;
    private static final int BOOK_SINGLE_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // The content URI of the form "content://hanaalalwi.bookapp/bookTable
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS, BOOKS);
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS + "/#", BOOK_SINGLE_ID);
    }

    private BookSQLHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new BookSQLHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                cursor = database.query(BookContract.BookEntryClass.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BOOK_SINGLE_ID:
                selection = BookContract.BookEntryClass._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(BookContract.BookEntryClass.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return insertBook(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertBook(Uri uri, ContentValues values) {//book name , book image , supplier name , supplier phone , supplier email
        String bookName = values.getAsString(BookContract.BookEntryClass.PRODUCT_NAME);
        if (bookName == null) {
            throw new IllegalArgumentException("The book must have a name !!");
        }
        Integer bookPrice = values.getAsInteger(BookContract.BookEntryClass.PRICE);
        if (bookPrice == null) {
            throw new IllegalArgumentException("The book must have a price !!");
        }
        Integer bookQuantity = values.getAsInteger(BookContract.BookEntryClass.PRICE);
        if (bookQuantity == null) {
            throw new IllegalArgumentException("what is the quantity !!");
        }
        SQLiteDatabase database = mHelper.getWritableDatabase();

        long id = database.insert(BookContract.BookEntryClass.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return updateBook(uri, contentValues, selection, selectionArgs);
            case BOOK_SINGLE_ID:
                selection = BookContract.BookEntryClass._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = mHelper.getWritableDatabase();
        int rowsUpdated = database.update(BookContract.BookEntryClass.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                rowsDeleted = database.delete(BookContract.BookEntryClass.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOK_SINGLE_ID:
                selection = BookContract.BookEntryClass._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(BookContract.BookEntryClass.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return BookContract.BookEntryClass.CONTENT_LIST_TYPE;
            case BOOK_SINGLE_ID:
                return BookContract.BookEntryClass.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}



