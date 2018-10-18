package hanaalalwi.bookapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import hanaalalwi.bookapp.data.BookContract;

/**
 * Created by HanaAlalwi on 1/26/2018.
 */

public class CursorAdapter extends android.widget.CursorAdapter {
    public CursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.activity_list_item, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        TextView nameOfOProduct = (TextView) view.findViewById(R.id.name);
        TextView priceOfProduct = (TextView) view.findViewById(R.id.summary);
        final TextView QuantityOfProduct = (TextView) view.findViewById(R.id.QuantityInList);
        Button Sale = (Button) view.findViewById(R.id.sale);

        int nameColumnIndex = cursor.getColumnIndex(BookContract.BookEntryClass.PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntryClass.PRICE);
        final int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntryClass.QUANTITY);

        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        final int productQuan = cursor.getInt(quantityColumnIndex);

        nameOfOProduct.setText(productName);
        priceOfProduct.setText(productPrice + "$");
        QuantityOfProduct.setText("" + productQuan);

        final int id1 = cursor.getInt(cursor.getColumnIndex(BookContract.BookEntryClass._ID));
        final ContentValues values = new ContentValues();

        Sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = productQuan;
                if (productQuan > 0) {
                    values.put(BookContract.BookEntryClass.QUANTITY, productQuan - 1);
                    context.getContentResolver().update(Uri.withAppendedPath(BookContract.BookEntryClass.CONTENT_URI, "" + id1)
                            , values, null, null);
                }
            }
        });

    }
}



