package com.pieter.declercq.datevalidator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.pieter.declercq.datevalidator.db.Database;
import com.pieter.declercq.datevalidator.domain.Category;
import com.pieter.declercq.datevalidator.domain.ExpiryProduct;
import com.pieter.declercq.datevalidator.domain.Product;
import com.pieter.declercq.datevalidator.exception.domain.DomainException;
import com.pieter.declercq.datevalidator.exception.service.ServiceException;
import com.pieter.declercq.datevalidator.service.DateValidator;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Pieter on 19/12/2014.
 */
public class DatabaseReaderActivity extends Activity {

    private final String databaseFileName = "ProductDatabase.txt";
    private final String expiryListFileName = "ExpiryList.txt";
    BufferedReader reader = null;
    DateValidator mDateValidator;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        try {
            mDateValidator = new DateValidator();
            mDateValidator.addCategory(new Category("zuivel", Color.rgb(238, 238, 238)));
            mDateValidator.addCategory(new Category("voeding", Color.rgb(255,161,53)));
            mDateValidator.addCategory(new Category("kaas", Color.rgb(255,255,53)));
            mDateValidator.addCategory(new Category("charcuterie", Color.rgb(197,87,70)));
            mDateValidator.addCategory(new Category("diepvries", Color.rgb(53,208,255)));
        } catch(ServiceException e){
            Log.d("PROXY", e.getMessage());
        } catch(DomainException e) {
            Log.d("PROXY", e.getMessage());
        }

        readProductDatabase();
        readExpiryList();

        Log.d("PROXY", "" + mDateValidator.getNumberOfProducts());
        Log.d("PROXY", "" + mDateValidator.getNumberOfExpiryProducts());

        Intent i = new Intent(this, MainMenuActivity.class);
        startActivity(i);
        DatabaseReaderActivity.this.finish();
    }

    public void readProductDatabase(){
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open(databaseFileName)));
            String mLine = "";

            while((mLine = reader.readLine()) != null) {
                String[] values = mLine.split("\\t");
                String ean = values[0];
                String name = values[1];
                String hope = values[2];
                String category = values[3];
                ArrayList<Category> categories = mDateValidator.getCategories();
                if (!categories.contains(new Category(category))){
                    mDateValidator.addCategory(new Category(category));
                }
                mDateValidator.addProduct(new Product(Long.parseLong(ean), name, Integer.parseInt(hope), new Category(category)));
            }
            reader.close();
        } catch (IOException e) {
            Log.d("PROXY", e.getMessage());
        } catch (DomainException e) {
            Log.d("PROXY", e.getMessage());
        } catch (ServiceException e) {
            Log.d("PROXY", e.getMessage());
        }

    }

    public void readExpiryList() {
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open(expiryListFileName)));
            String mLine = "";

            while ((mLine = reader.readLine()) != null) {
                String[] values = mLine.split("\\t");
                Category category = new Category(values[0]);
                Long ean = Long.parseLong(values[1]);
                int day = Integer.parseInt(values[2]);
                int month = Integer.parseInt(values[3]);
                int year = Integer.parseInt(values[4]);
                int spot = Integer.parseInt(values[5]);
                boolean removed = Boolean.parseBoolean(values[6]);
                ArrayList<Category> categories = mDateValidator.getCategories();
                if (!categories.contains(category)){
                    mDateValidator.addCategory(category);
                }
                Product p = mDateValidator.getProduct(category, ean);
                mDateValidator.addExpiryProduct(new ExpiryProduct(p, day, month, year, spot, removed));
            }
            reader.close();
        } catch (IOException e) {
            Log.d("PROXY", e.getMessage());
        } catch (DomainException e){
            Log.d("PROXY", e.getMessage());
        } catch (ServiceException e){
            Log.d("PROXY", e.getMessage());
        }
    }
}