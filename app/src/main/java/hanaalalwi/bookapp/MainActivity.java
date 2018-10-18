package hanaalalwi.bookapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import hanaalalwi.bookapp.data.BookContract;
import hanaalalwi.bookapp.data.BookContract.BookEntryClass;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final int BOOK_LOADER = 0;
    CursorAdapter bookCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView BookListView = (ListView) findViewById(R.id.BookListView);
        View emptyView = findViewById(R.id.empty_view);
        BookListView.setEmptyView(emptyView);

        bookCursorAdapter = new CursorAdapter(this, null);
        BookListView.setAdapter(bookCursorAdapter);

        BookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Uri currentBookUri = ContentUris.withAppendedId(BookContract.BookEntryClass.CONTENT_URI, id);
                Log.e("MainActiviy", currentBookUri.toString());
                Intent intent = new Intent(MainActivity.this, ViewDetails.class);
                intent.setData(currentBookUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }

    private void deleteAllBooks() {
        int rowsDeleted = getContentResolver().delete(BookEntryClass.CONTENT_URI, null, null);
        Log.v("EditActivity", rowsDeleted + " rows deleted from Book database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(MainActivity.this, AddBook.class);
                startActivity(intent);
                return true;
            case R.id.delete:
                deleteDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String projection[] = {
                BookEntryClass._ID,
                BookEntryClass.PRODUCT_NAME,
                BookEntryClass.PRICE,
                BookEntryClass.QUANTITY,
                BookEntryClass.SUPPLIER_NAME,
                BookEntryClass.SUPPLIER_EMAIL,
                BookEntryClass.SUPPLIER_PHONE_NUMBER
        };
        return new CursorLoader(this,
                BookEntryClass.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        bookCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bookCursorAdapter.swapCursor(null);
    }

    private void deleteDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                deleteAllBooks();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}

