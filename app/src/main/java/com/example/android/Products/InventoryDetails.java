package com.example.android.Products;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Data.productsContract;
import Data.productsDbHelper;

public class InventoryDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_PRODUCT_LOADER = 0;
    private Uri currentUri;
    private EditText productName;
    private EditText price;
    private EditText quantity;
    private EditText supplierName;
    private EditText supplierPhone;
    private boolean mProductHasChanged;
    Button Increase;
    Button Decrease;
    Button Supply;
    Button Delete;
    Button Update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_details);
        this.setTitle(R.string.Product_Details);
        Intent intent = getIntent();
        currentUri = intent.getData();
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
        if (currentUri != null) {
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        } else {
            Intent addProduct = new Intent(InventoryDetails.this, AddInventory.class);
            startActivity(addProduct);
        }

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
                showDeleteConfirmationDialog();
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
                updateProduct();
            }
        });
    }


    private void updateProduct() {
        String PName = productName.getText().toString().trim();
        String PPrice = price.getText().toString().trim();
        String PQuantity = quantity.getText().toString().trim();
        String SName = supplierName.getText().toString().trim();
        String SPhone = supplierPhone.getText().toString().trim();
        ContentValues values=new ContentValues();
        values.put(productsContract.productEntry.COLUMN_PRODUCT_NAME,PName);
        values.put(productsContract.productEntry.COLUMN_PRODUCT_PRICE,Float.parseFloat(PPrice));
        values.put(productsContract.productEntry.COLUMN_PRODUCT_QUANTITY,Integer.parseInt(PQuantity));
        values.put(productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERNAME,SName);
        values.put(productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERPHONE,Integer.parseInt(SPhone));
        getContentResolver().update(currentUri,values,null,null);
    }

    private void subOne() {
        String Quantity = quantity.getText().toString();
        int productQuantity = Integer.parseInt(Quantity);
        if (Quantity.isEmpty()) {
            productQuantity = 0;
        } else if (productQuantity > 0) {
            productQuantity = productQuantity - 1;
        }
        quantity.setText(String.valueOf(productQuantity));
    }


    private void deleteItem() {
        getContentResolver().delete(currentUri, null, null);
        Intent intent = new Intent(this, DisplayActivity.class);
        startActivity(intent);
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
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteItem();
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
        String[] projection = {
                productsContract.productEntry._ID,
                productsContract.productEntry.COLUMN_PRODUCT_NAME,
                productsContract.productEntry.COLUMN_PRODUCT_PRICE,
                productsContract.productEntry.COLUMN_PRODUCT_QUANTITY,
                productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERNAME,
                productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERPHONE
        };
        return new CursorLoader(this,
                currentUri,
                projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
       if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            productName.setText(cursor.getString(cursor.getColumnIndex(productsContract.productEntry.COLUMN_PRODUCT_NAME)));
            price.setText(cursor.getString(cursor.getColumnIndex(productsContract.productEntry.COLUMN_PRODUCT_PRICE)));
            quantity.setText(cursor.getString(cursor.getColumnIndex(productsContract.productEntry.COLUMN_PRODUCT_QUANTITY)));
            supplierName.setText(cursor.getString(cursor.getColumnIndex(productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERNAME)));
            supplierPhone.setText(cursor.getString(cursor.getColumnIndex(productsContract.productEntry.COLUMN_PRODUCT_SUPPLIERPHONE)));

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productName.setText("");
        price.setText("");
        quantity.setText("");
        supplierName.setText("");
        supplierPhone.setText("");
    }
}
