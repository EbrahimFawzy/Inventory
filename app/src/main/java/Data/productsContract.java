package Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class productsContract {
    public productsContract() {
    }

    public static final class productEntry implements BaseColumns {
        public static final String CONTENT_AUTHOURITY = "com.example.android.Products";
        public static final String PATH_PRODUCTS = "products";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHOURITY);
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHOURITY + "/" + PATH_PRODUCTS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHOURITY + "/" + PATH_PRODUCTS;
        public final static String TABLE_NAME = "products";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PRODUCT_NAME = "ProductName";
        public final static String COLUMN_PRODUCT_PRICE = "Price";
        public final static String COLUMN_PRODUCT_QUANTITY = "Quantity";
        public final static String COLUMN_PRODUCT_SUPPLIERNAME = "SupplierName";
        public final static String COLUMN_PRODUCT_SUPPLIERPHONE = "SupplierPhone";
    }
}
