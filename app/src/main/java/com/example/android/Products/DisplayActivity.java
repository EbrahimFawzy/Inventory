package com.example.android.Products;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;

import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import Data.productsContract;
import Data.productsDbHelper;

public class DisplayActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private final static int PRODUCT_LOADER = 0;
    public productCursorAdapter adapter;
    public productsDbHelper mProductsDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        this.setTitle(R.string.Inventories);
        adapter = new productCursorAdapter(this, null);
        ListView productListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        productListView.setEmptyView(emptyView);
        productListView.setAdapter(adapter);
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
    }

    public void clickOnItem(long id) {
        Intent intent = new Intent(DisplayActivity.this, InventoryDetails.class);
        Uri currentProductUri = ContentUris.withAppendedId(productsContract.productEntry.CONTENT_URI, id);
        intent.setData(currentProductUri);
        startActivity(intent);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String[] Projection = {
                productsContract.productEntry._ID,
                productsContract.productEntry.COLUMN_PRODUCT_NAME,
                productsContract.productEntry.COLUMN_PRODUCT_PRICE,
                productsContract.productEntry.COLUMN_PRODUCT_QUANTITY,
                productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERNAME,
                productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERPHONE
        };
        return new CursorLoader(this,
                productsContract.productEntry.CONTENT_URI,
                Projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }

    public void sell(long id, int productQuantity) {
        mProductsDbHelper = new productsDbHelper(this);
        mProductsDbHelper.sellItem(id, productQuantity);
        adapter.swapCursor(mProductsDbHelper.readData());
    }
}
