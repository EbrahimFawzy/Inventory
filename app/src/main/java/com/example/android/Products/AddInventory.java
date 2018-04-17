package com.example.android.Products;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddInventory extends AppCompatActivity {
    private EditText productName;
    private EditText price;
    private EditText quantity;
    private EditText supplierName;
    private EditText supplierPhone;
    private Uri mCurrentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);
        this.setTitle(R.string.Add_Inventory);
        Intent intent = getIntent();
        mCurrentUri = intent.getData();
        productName = findViewById(R.id.nameEditText);
        price = findViewById(R.id.priceEditText);
        quantity = findViewById(R.id.quantityEditText);
        supplierName = findViewById(R.id.supplierEditText);
        supplierPhone = findViewById(R.id.supplierPhonEditText);
        Button save_btn = findViewById(R.id.save_inventory);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInventory();
                finish();
            }
        });
    }

    public void addInventory() {
        String PName = productName.getText().toString().trim();
        String PPrice = price.getText().toString().trim();
        String PQuantity = quantity.getText().toString().trim();
        String SName = supplierName.getText().toString().trim();
        String SPhone = supplierPhone.getText().toString().trim();
        if (mCurrentUri == null && TextUtils.isEmpty(PName) && TextUtils.isEmpty(PPrice) &&
                TextUtils.isEmpty(PQuantity) && TextUtils.isEmpty(SName) && TextUtils.isEmpty(SPhone)) {
            Toast.makeText(this, R.string.fillData, Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(PName)) {
            Toast.makeText(this, R.string.Enter_Product_Name, Toast.LENGTH_LONG).show();
            return;
        }
        float price = 0;
        if (!(TextUtils.isEmpty(PPrice)) && Float.parseFloat(PPrice) > 0) {
            price = Float.parseFloat(PPrice);
        } else {
            Toast.makeText(this, R.string.invalid_price, Toast.LENGTH_LONG).show();
            return;
        }
        int quantity = 0;
        if (!(TextUtils.isEmpty(PQuantity)) && Integer.parseInt(PQuantity) > 0) {
            quantity = Integer.parseInt(PQuantity);
        } else {
            Toast.makeText(this, R.string.invalid_quantity, Toast.LENGTH_LONG).show();
            return;
        }
        int phone = 0;
        if (!(TextUtils.isEmpty(SPhone)) && Integer.parseInt(SPhone) > 0) {
            phone = Integer.parseInt(SPhone);
        } else {
            Toast.makeText(this, R.string.invalid_phone, Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(SName)) {
            Toast.makeText(this, R.string.Enter_Supplier_Name, Toast.LENGTH_LONG).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(Data.productsContract.productEntry.COLUMN_PRODUCT_NAME, PName);
        values.put(Data.productsContract.productEntry.COLUMN_PRODUCT_PRICE, price);
        values.put(Data.productsContract.productEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(Data.productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERNAME, SName);
        values.put(Data.productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERPHONE, phone);
        if (mCurrentUri == null) {
            Uri newUri = getContentResolver().insert(Data.productsContract.productEntry.CONTENT_URI,
                    values);
            if (newUri == null) {
                Toast.makeText(this, R.string.error_saving, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.inventory_saved, Toast.LENGTH_LONG).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, R.string.update_failed, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.update_success, Toast.LENGTH_LONG).show();
            }
        }
    }
}
