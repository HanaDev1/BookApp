package hanaalalwi.bookapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hanaalalwi.bookapp.data.BookContract.BookEntryClass;

/**
 * Created by HanaAlalwi on 1/26/2018.
 */

public class EditActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_BOOK_LOADER = 0;
    private Uri currentBOOKUri;
    private EditText mNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mSupNameEditText;
    private EditText mSupEmailEditText;
    private EditText mSupPhoneEditText;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        currentBOOKUri = intent.getData();

        getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);

        mNameEditText = (EditText) findViewById(R.id.book_name);
        mPriceEditText = (EditText) findViewById(R.id.price);
        mQuantityEditText = (EditText) findViewById(R.id.quantity);

        mSupNameEditText = (EditText) findViewById(R.id.supName);
        mSupEmailEditText = (EditText) findViewById(R.id.supEmail);
        mSupPhoneEditText = (EditText) findViewById(R.id.supPhone);

        Button saveEdit = (Button) findViewById(R.id.saveChanges);
        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLastBookEdit();
            }
        });

        Button DeleteBook = (Button) findViewById(R.id.deleteBook);
        DeleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSingleBookDialog();
            }
        });
    }

    private void saveLastBookEdit() {
        String productName = mNameEditText.getText().toString().trim();
        String price = mPriceEditText.getText().toString().trim();
        String quantity = mQuantityEditText.getText().toString().trim();
        String suplierNameValue = mSupNameEditText.getText().toString().trim();
        String suplierEmailValue = mSupEmailEditText.getText().toString().trim();
        String suplierPhoneValue = mSupPhoneEditText.getText().toString().trim();

        if (currentBOOKUri == null &&
                TextUtils.isEmpty(productName) && TextUtils.isEmpty(suplierNameValue) && TextUtils.isEmpty(suplierEmailValue)) {
            return;

        } else if (!TextUtils.isEmpty(quantity) && !TextUtils.isEmpty(productName) && !TextUtils.isEmpty(price) && !TextUtils.isEmpty(suplierNameValue) && !TextUtils.isEmpty(suplierEmailValue) && !TextUtils.isEmpty(suplierPhoneValue)) {
            {

                ContentValues values = new ContentValues();
                values.put(BookEntryClass.PRODUCT_NAME, productName);
                values.put(BookEntryClass.SUPPLIER_EMAIL, suplierEmailValue);
                values.put(BookEntryClass.SUPPLIER_NAME, suplierNameValue);

                int PriceValue = 0;
                int QuanValue = 0;
                long phone = 0;
                if (!TextUtils.isEmpty(price) && !TextUtils.isEmpty(quantity) && !TextUtils.isEmpty(suplierPhoneValue)) {
                    PriceValue = Integer.parseInt(price);
                    QuanValue = Integer.parseInt(quantity);
                    phone = Long.parseLong(suplierPhoneValue);
                }

                values.put(BookEntryClass.PRICE, PriceValue);
                values.put(BookEntryClass.QUANTITY, QuanValue);
                values.put(BookEntryClass.SUPPLIER_PHONE_NUMBER, phone);

                if (currentBOOKUri == null) {
                    Uri newUri = getContentResolver().insert(BookEntryClass.CONTENT_URI, values);
                    if (newUri == null) {
                        Toast.makeText(this, getString(R.string.FaildInsert),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getString(R.string.SuccessInsert),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    int rowsAffected = getContentResolver().update(currentBOOKUri, values, null, null);
                    if (rowsAffected == 0) {
                        Toast.makeText(this, getString(R.string.FaildUpdate),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getString(R.string.SuccessUpdate),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                finish();
            }
        } else {
            Toast messageQuan = new Toast(getApplicationContext());
            messageQuan.makeText(getApplicationContext(), "Please complete all information ", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteBook() {
        if (currentBOOKUri != null) {
            int rowsDeleted = getContentResolver().delete(currentBOOKUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.FaildDel),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.SuccessDel),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
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
                currentBOOKUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {

        if (data == null || data.getCount() < 1) {
            return;
        }
        if (data.moveToFirst()) {
            int productNameColumnIndex = data.getColumnIndex(BookEntryClass.PRODUCT_NAME);
            int priceColumnIndex = data.getColumnIndex(BookEntryClass.PRICE);
            int quantityColumnIndex = data.getColumnIndex(BookEntryClass.QUANTITY);
            int supNameColumnIndex = data.getColumnIndex(BookEntryClass.SUPPLIER_NAME);
            int supEmailColumnIndex = data.getColumnIndex(BookEntryClass.SUPPLIER_EMAIL);
            int supPhoneColumnIndex = data.getColumnIndex(BookEntryClass.SUPPLIER_PHONE_NUMBER);

            String currentName = data.getString(productNameColumnIndex);
            int currentPrice = data.getInt(priceColumnIndex);
            int currentQuantity = data.getInt(quantityColumnIndex);
            String currentSupName = data.getString(supNameColumnIndex);
            final String currentSupEmail = data.getString(supEmailColumnIndex);
            final long currentSupPhone = data.getLong(supPhoneColumnIndex);

            mNameEditText.setText(currentName);
            mPriceEditText.setText(Integer.toString(currentPrice));
            mQuantityEditText.setText(Integer.toString(currentQuantity));
            mSupNameEditText.setText(currentSupName);
            mSupEmailEditText.setText(currentSupEmail);
            mSupPhoneEditText.setText(Long.toString(currentSupPhone));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupNameEditText.setText("");
        mSupEmailEditText.setText("");
        mSupPhoneEditText.setText("");

    }

    private void deleteSingleBookDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg2);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                deleteBook();
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
