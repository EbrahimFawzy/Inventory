package com.example.android.Products;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import Data.productsContract;

public class productCursorAdapter extends CursorAdapter {

    private final DisplayActivity displayActivity;

    public productCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        this.displayActivity = (DisplayActivity) context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(final View view, Context context, final Cursor cursor) {
        TextView name = view.findViewById(R.id.name);
        TextView price = view.findViewById(R.id.price);
        TextView quantity = view.findViewById(R.id.quantity);
        Button saleBtn = view.findViewById(R.id.sell);
        int nameColumnIndex = cursor.getColumnIndex(productsContract.productEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(productsContract.productEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(productsContract.productEntry.COLUMN_PRODUCT_QUANTITY);

        String productName = cursor.getString(nameColumnIndex);
        int productPrice = cursor.getInt(priceColumnIndex);
        final int productQuantity = cursor.getInt(quantityColumnIndex);

        name.setText(productName);
        price.setText(String.valueOf(productPrice));
        quantity.setText(String.valueOf(productQuantity));
        final long id = cursor.getLong(cursor.getColumnIndex(productsContract.productEntry._ID));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayActivity.clickOnItem(id);
            }
        });

        saleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayActivity.sell(id, productQuantity);
            }
        });
    }
}
