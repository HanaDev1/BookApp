package hanaalalwi.bookapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import hanaalalwi.bookapp.data.BookContract.BookEntryClass;

public class ViewDetails extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_BOOK_LOADER = 0;
    private Uri currentBOOKUri;

    private TextView mNameTextView;
    private TextView mPriceTextView;
    private TextView mQuantityTextView;
    private TextView mSupNameTextView;
    private TextView mSupEmailTextView;
    private TextView mSupPhoneTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        Intent intent = getIntent();
        currentBOOKUri = intent.getData();

        getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);

        mNameTextView = (TextView) findViewById(R.id.book_name1);
        mPriceTextView = (TextView) findViewById(R.id.price1);
        mQuantityTextView = (TextView) findViewById(R.id.quantity1);

        mSupNameTextView = (TextView) findViewById(R.id.supName1);
        mSupEmailTextView = (TextView) findViewById(R.id.supEmail1);
        mSupPhoneTextView = (TextView) findViewById(R.id.supPhone1);

        Button EditeBook = (Button) findViewById(R.id.Toedit);
        EditeBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewDetails.this, EditActivity.class);
                intent.setData(currentBOOKUri);
                startActivity(intent);
            }
        });

        Button DeleteBook = (Button) findViewById(R.id.deleteBook);
        DeleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSingleBookDialog();
            }
        });

        Button saveEdit = (Button) findViewById(R.id.saveChanges);
        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuantityEdit();
            }
        });

    }

    private void saveQuantityEdit() {
        String quantity = mQuantityTextView.getText().toString().trim();

        ContentValues values = new ContentValues();

        int QuanValue = 0;

        if (!TextUtils.isEmpty(quantity)) {
            QuanValue = Integer.parseInt(quantity);
        }

        values.put(BookEntryClass.QUANTITY, QuanValue);

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
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
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

            TextView contact1 = (TextView) findViewById(R.id.contactByEmail);
            contact1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent email = new Intent(Intent.ACTION_SENDTO);
                    email.setData(Uri.parse("mailto:" + currentSupEmail));
                    startActivity(email);
                }
            });

            TextView contact2 = (TextView) findViewById(R.id.contactByPhone);
            contact2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", Long.toString(currentSupPhone), null));
                    if (ActivityCompat.checkSelfPermission(ViewDetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(callIntent);

                }
            });

            Button plus = (Button) findViewById(R.id.plusButton);
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Quan = String.valueOf(mQuantityTextView.getText());
                    int intQuan = Integer.parseInt(Quan);
                    intQuan = intQuan + 1;
                    mQuantityTextView.setText(Integer.toString(intQuan));

                }
            });

            Button minus = (Button) findViewById(R.id.minusButton);
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Quan = String.valueOf(mQuantityTextView.getText());
                    int intQuan = Integer.parseInt(Quan);
                    intQuan = intQuan - 1;
                    if (intQuan < 0) {
                        intQuan = 0;
                    }
                    mQuantityTextView.setText(Integer.toString(intQuan));

                }
            });

            mNameTextView.setText(currentName);
            mPriceTextView.setText(Integer.toString(currentPrice));
            mQuantityTextView.setText(Integer.toString(currentQuantity));
            mSupNameTextView.setText(currentSupName);
            mSupEmailTextView.setText(currentSupEmail);
            mSupPhoneTextView.setText(Long.toString(currentSupPhone));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameTextView.setText("");
        mPriceTextView.setText("");
        mQuantityTextView.setText("");
        mSupNameTextView.setText("");
        mSupEmailTextView.setText("");
        mSupPhoneTextView.setText("");
    }
}
