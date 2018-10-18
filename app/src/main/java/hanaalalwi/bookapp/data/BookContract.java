package hanaalalwi.bookapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by HanaAlalwi on 1/23/2018.
 */

public class BookContract {
    private BookContract() {
    }

    public static final String CONTENT_AUTHORITY = "hanaalalwi.bookapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BOOKS = "BookTable";

    public static final class BookEntryClass implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        public final static String TABLE_NAME = "BookTable";

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public final static String PRODUCT_NAME = "product_name";
        public final static String PRICE = "product_price";
        public final static String QUANTITY = "product_quantity";

        public final static String SUPPLIER_NAME = "supplier_name";
        public final static String SUPPLIER_EMAIL = "supplier_email";
        public final static String SUPPLIER_PHONE_NUMBER = "phoneNumber";

    }
}
