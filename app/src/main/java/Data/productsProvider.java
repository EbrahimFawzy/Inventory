package Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class productsProvider extends ContentProvider {
    private productsDbHelper mproducts;
    public static final String LOG_TAG = productsProvider.class.getSimpleName();
    private static final int products = 100;
    private static final int productsID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(productsContract.productEntry.CONTENT_AUTHOURITY, productsContract.productEntry.PATH_PRODUCTS, products);
        sUriMatcher.addURI(productsContract.productEntry.CONTENT_AUTHOURITY, productsContract.productEntry.PATH_PRODUCTS + "/#", productsID);
    }

    @Override
    public boolean onCreate() {
        mproducts = new productsDbHelper(getContext());
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mproducts.getReadableDatabase();
        Cursor cursor;
        int matcher = sUriMatcher.match(uri);
        switch (matcher) {
            case products:
                cursor = database.query(productsContract.productEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, null, sortOrder);
                break;
            case productsID:
                selection = productsContract.productEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(productsContract.productEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case products:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    public Uri insertProduct(Uri uri, ContentValues values) {
        String name = values.getAsString(productsContract.productEntry.COLUMN_PRODUCT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("product requires name");
        }
        Float price = values.getAsFloat(productsContract.productEntry.COLUMN_PRODUCT_PRICE);
        if (price != null && price < 0 || price == null) {
            throw new IllegalArgumentException("product requires valid price");
        }
        Integer quantity = values.getAsInteger(productsContract.productEntry.COLUMN_PRODUCT_QUANTITY);
        if (quantity != null && quantity < 0 || quantity == null) {
            throw new IllegalArgumentException("Products requires valid quantity");
        }
        String supplierName = values.getAsString(productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERNAME);
        if (supplierName == null) {
            throw new IllegalArgumentException("product requires supplier name");
        }
        Integer supplierPhone = values.getAsInteger(productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERPHONE);
        if (supplierPhone != null && supplierPhone < 0 || supplierPhone == null) {
            throw new IllegalArgumentException("product requires valid supplier phone");
        }
        SQLiteDatabase database = mproducts.getReadableDatabase();
        long id = database.insert(productsContract.productEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (values.containsKey(productsContract.productEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(productsContract.productEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product requires valid name");
            }
        }
        if (values.containsKey(productsContract.productEntry.COLUMN_PRODUCT_PRICE)) {
            Integer price = values.getAsInteger(productsContract.productEntry.COLUMN_PRODUCT_PRICE);
            if (price == null || price < 0) {
                throw new IllegalArgumentException("Product requires valid price");
            }
        }
        if (values.containsKey(productsContract.productEntry.COLUMN_PRODUCT_QUANTITY)) {
            Integer quantity = values.getAsInteger(productsContract.productEntry.COLUMN_PRODUCT_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("product requires valid quantity");
            }
        }
        if (values.containsKey(productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERNAME)) {
            String sName = values.getAsString(productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERNAME);
            if (sName == null) {
                throw new IllegalArgumentException("Product requier Supplier Name");
            }
        }
        if (values.containsKey(productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERPHONE)) {
            Integer sName = values.getAsInteger(productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERPHONE);
            if (sName == null) {
                throw new IllegalArgumentException("Product requier Supplier Phone");
            }
        }
        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = mproducts.getWritableDatabase();
        int rowUpdated = database.update(productsContract.productEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mproducts.getWritableDatabase();
        int rowDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case products:
                rowDeleted = database.delete(productsContract.productEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case productsID:
                selection = productsContract.productEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                getContext().getContentResolver().notifyChange(uri, null);
                rowDeleted = database.delete(productsContract.productEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case products:
                return productsContract.productEntry.CONTENT_LIST_TYPE;
            case productsID:
                return productsContract.productEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown uri " + uri + " with match " + match);
        }
    }
}
