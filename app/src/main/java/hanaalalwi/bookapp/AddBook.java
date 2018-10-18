package hanaalalwi.bookapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hanaalalwi.bookapp.data.BookContract.BookEntryClass;
import hanaalalwi.bookapp.data.BookSQLHelper;

/**
 * Created by HanaAlalwi on 1/25/2018.
 */

public class AddBook extends AppCompatActivity {
    private EditText mNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mSupNameEditText;
    private EditText mSupEmailEditText;
    private EditText mSupPhoneEditText;
    BookSQLHelper mDbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mNameEditText = (EditText) findViewById(R.id.book_name);
        mPriceEditText = (EditText) findViewById(R.id.price);
        mQuantityEditText = (EditText) findViewById(R.id.quantity);
        mSupNameEditText = (EditText) findViewById(R.id.supName);
        mSupEmailEditText = (EditText) findViewById(R.id.supEmail);
        mSupPhoneEditText = (EditText) findViewById(R.id.supPhone);

        mDbHelper = new BookSQLHelper(this);

        Button saveBooks = (Button) findViewById(R.id.SaveData);
        saveBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertBooks();
            }
        });
    }

    private void insertBooks() {
        String productName = mNameEditText.getText().toString().trim();
        String price = mPriceEditText.getText().toString().trim();
        String quantity = mQuantityEditText.getText().toString().trim();
        String supName = mSupNameEditText.getText().toString().trim();
        String supEmail = mSupEmailEditText.getText().toString().trim();
        String supPhone = mSupPhoneEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(quantity) && !TextUtils.isEmpty(productName) && !TextUtils.isEmpty(price) && !TextUtils.isEmpty(supName) && !TextUtils.isEmpty(supEmail) && !TextUtils.isEmpty(supPhone)) {

            int PriceValue = Integer.parseInt(price);

            mDbHelper = new BookSQLHelper(this);

            db = mDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(BookEntryClass.PRODUCT_NAME, productName);
            values.put(BookEntryClass.PRICE, PriceValue);

            int QuanValue = Integer.parseInt(quantity);

            if (QuanValue > 0) {
                values.put(BookEntryClass.QUANTITY, QuanValue);
            } else {
                QuanValue = 0;
                values.put(BookEntryClass.QUANTITY, QuanValue);
            }
            values.put(BookEntryClass.SUPPLIER_NAME, supName);
            values.put(BookEntryClass.SUPPLIER_EMAIL, supEmail);

            long phone = Long.parseLong(supPhone);
            values.put(BookEntryClass.SUPPLIER_PHONE_NUMBER, phone);

            Uri newUri = getContentResolver().insert(BookEntryClass.CONTENT_URI, values);
            if (newUri == null) {
            } else {
                Toast.makeText(this, "Books saved ", Toast.LENGTH_SHORT).show();

            }
            finish();

        } else {
            Toast messageQuan = new Toast(getApplicationContext());
            messageQuan.makeText(getApplicationContext(), "Please complete all information ", Toast.LENGTH_SHORT).show();
        }
    }

}
