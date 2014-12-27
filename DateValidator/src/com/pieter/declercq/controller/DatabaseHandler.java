package com.pieter.declercq.controller;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import com.pieter.declercq.datevalidator.domain.Category;
import com.pieter.declercq.datevalidator.domain.Product;
import com.pieter.declercq.datevalidator.exception.domain.DomainException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pieter on 19/12/2014.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "datumControleDatabase.db";

    // Contacts table name
    private static final String TABLE_PRODUCTS = "products";

    // Contacts Table Columns names
    private static final String KEY_EAN = "ean";
    private static final String KEY_NAME = "name";
    private static final String KEY_HOPE = "hope";
    private static final String KEY_CATEGORY = "category";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + KEY_EAN + " LONG PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_HOPE + " INTEGER," + KEY_CATEGORY + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EAN, product.getEan()); // Ean code
        values.put(KEY_NAME, product.getName()); // Product Name
        values.put(KEY_HOPE, product.getHope()); // Hope Number
        values.put(KEY_CATEGORY, product.getCategory().toString()); //Category

        // Inserting Row
        db.insert(TABLE_PRODUCTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public Product getProduct(long ean) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRODUCTS, new String[] { KEY_EAN,
                        KEY_NAME, KEY_HOPE, KEY_CATEGORY }, KEY_EAN + "=?",
                new String[] { String.valueOf(ean) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        try {
            Product product = new Product(Long.parseLong(cursor.getString(0)),
                    cursor.getString(1), Integer.parseInt(cursor.getString(2)), new Category(cursor.getString(3)));
            return product;
        } catch (DomainException e) {
            Log.d("Proxy", e.getMessage());
        }

        return null;
    }

    // Getting All Contacts
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<Product>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                try {
                    Product product = new Product();
                    product.setEan(Long.parseLong(cursor.getString(0)));
                    product.setName(cursor.getString(1));
                    product.setHope(Integer.parseInt(cursor.getString(2)));
                    product.setCategory(new Category(cursor.getString(3)));
                    // Adding contact to list
                    productList.add(product);
                } catch (DomainException e) {
                    Log.d("PROXY", e.getMessage());
                }
            } while (cursor.moveToNext());
        }

        // return contact list
        return productList;
    }

    // Getting contacts Count
    public int getProductsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating single contact
    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EAN, product.getEan()); // Ean code
        values.put(KEY_NAME, product.getName()); // Product Name
        values.put(KEY_HOPE, product.getHope()); // Hope Number
        values.put(KEY_CATEGORY, product.getCategory().toString()); //Category

        // updating row
        return db.update(TABLE_PRODUCTS, values, KEY_EAN + " = ?",
                new String[] { String.valueOf(product.getEan()) });
    }

    // Deleting single contact
    public void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, KEY_EAN + " = ?",
                new String[] { String.valueOf(product.getEan()) });
        db.close();
    }
}
