package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class productsDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = productsDbHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "Products.db";
    public static final int DATABASE_VERSION = 1;

    public productsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " + productsContract.productEntry.TABLE_NAME + " (" +
                productsContract.productEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                productsContract.productEntry.COLUMN_PRODUCT_NAME + " TEXT, " +
                productsContract.productEntry.COLUMN_PRODUCT_PRICE + " REAL, " +
                productsContract.productEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER DEFAULT 0, " +
                productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERNAME + " TEXT, " +
                productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERPHONE + " INTEGER);";
        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor readData() {
        SQLiteDatabase database = getReadableDatabase();
        String[] projection = {
                productsContract.productEntry._ID,
                productsContract.productEntry.COLUMN_PRODUCT_NAME,
                productsContract.productEntry.COLUMN_PRODUCT_PRICE,
                productsContract.productEntry.COLUMN_PRODUCT_QUANTITY,
                productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERNAME,
                productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERPHONE
        };
        Cursor cursor = database.query(
                productsContract.productEntry.TABLE_NAME,
                projection,
                null, null, null, null, null
        );
        return cursor;
    }

    public void sellItem(long id, int i) {
        SQLiteDatabase database = getWritableDatabase();
        int quantity = 0;
        if (i > 0) {
            quantity = i - 1;
        }
        ContentValues values = new ContentValues();
        values.put(productsContract.productEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        String selection = productsContract.productEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        database.update(productsContract.productEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public void updateItem(long id, int i) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(productsContract.productEntry.COLUMN_PRODUCT_QUANTITY, i);
        String selection = productsContract.productEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        database.update(productsContract.productEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public void updateItem(long id, String name, float price, int quantity, String sName, int sPhone) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(productsContract.productEntry.COLUMN_PRODUCT_NAME, name);
        values.put(productsContract.productEntry.COLUMN_PRODUCT_PRICE, price);
        values.put(productsContract.productEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERNAME, sName);
        values.put(productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERPHONE, sPhone);
        String selection = productsContract.productEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        database.update(productsContract.productEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public Cursor showItem(long currentItem) {
        SQLiteDatabase database = getReadableDatabase();
        String[] projection = {
                productsContract.productEntry._ID,
                productsContract.productEntry.COLUMN_PRODUCT_NAME,
                productsContract.productEntry.COLUMN_PRODUCT_PRICE,
                productsContract.productEntry.COLUMN_PRODUCT_QUANTITY,
                productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERNAME,
                productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERPHONE
        };
        String selection = productsContract.productEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(currentItem)};
        Cursor cursor = database.query(
                productsContract.productEntry.TABLE_NAME,
                projection, selection, selectionArgs, null, null, null
        );
        return cursor;
    }
}
