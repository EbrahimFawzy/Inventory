package com.example.android.Products;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import Data.productsContract;
import Data.productsDbHelper;

public class InventoryDetails extends AppCompatActivity {

    productsDbHelper productsDbHelper;
    long currentItem;
    private EditText productName;
    private EditText price;
    private EditText quantity;
    private EditText supplierName;
    private EditText supplierPhone;
    Button Increase;
    Button Decrease;
    Button Supply;
    Button Delete;
    Button Update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_details);
        productName = findViewById(R.id.nameEditText);
        price = findViewById(R.id.priceEditText);
        quantity = findViewById(R.id.quantityEditText);
        supplierName = findViewById(R.id.supplierEditText);
        supplierPhone = findViewById(R.id.supplierPhonEditText);
        Increase = findViewById(R.id.increase);
        Decrease = findViewById(R.id.decrease);
        Supply = findViewById(R.id.supply);
        Delete = findViewById(R.id.delete);
        Update = findViewById(R.id.update);

        productsDbHelper = new productsDbHelper(this);
        currentItem = getIntent().getLongExtra("Id", 0);
        Cursor cursor = productsDbHelper.showItem(currentItem);
        cursor.moveToFirst();

        productName.setText(cursor.getString(cursor.getColumnIndex(productsContract.productEntry.COLUMN_PRODUCT_NAME)));
        price.setText(cursor.getString(cursor.getColumnIndex(productsContract.productEntry.COLUMN_PRODUCT_PRICE)));
        quantity.setText(cursor.getString(cursor.getColumnIndex(productsContract.productEntry.COLUMN_PRODUCT_QUANTITY)));
        supplierName.setText(cursor.getString(cursor.getColumnIndex(productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERNAME)));
        supplierPhone.setText(cursor.getString(cursor.getColumnIndex(productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERPHONE)));

        Increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOne();
            }
        });

        Decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subOne();
            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(currentItem);
                finish();
            }
        });
        Supply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + supplierPhone.getText().toString().trim()));
                startActivity(intent);
            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct(currentItem);
            }
        });
    }

    private void updateProduct(long currentItem) {
        String Name = productName.getText().toString();
        float Price = Float.parseFloat(price.getText().toString());
        int Quantity = Integer.parseInt(quantity.getText().toString());
        String sName = supplierName.getText().toString();
        int sPhone = Integer.parseInt(supplierPhone.getText().toString());
        productsDbHelper.updateItem(currentItem, Name, Price, Quantity, sName, sPhone);
    }

    private void deleteItem(long id) {
        SQLiteDatabase database = productsDbHelper.getWritableDatabase();
        String selection = productsContract.productEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};
        database.delete(productsContract.productEntry.TABLE_NAME,
                selection, selectionArgs);
        Intent intent = new Intent(this, DisplayActivity.class);
        startActivity(intent);
    }

    private void subOne() {
        String Quantity = quantity.getText().toString();
        int productQuantity = Integer.parseInt(Quantity);
        if (productQuantity > 0) {
            productQuantity = productQuantity - 1;
            quantity.setText(String.valueOf(productQuantity));
            productsDbHelper.updateItem(currentItem, productQuantity);
        }
    }

    private void addOne() {
        String Quantity = quantity.getText().toString();
        int productQuantity = Integer.parseInt(Quantity);
        if (Quantity.isEmpty()) {
            productQuantity = 0;
        } else {
            productQuantity = productQuantity + 1;
        }
        quantity.setText(String.valueOf(productQuantity));
        productsDbHelper.updateItem(currentItem, productQuantity);
    }
}
