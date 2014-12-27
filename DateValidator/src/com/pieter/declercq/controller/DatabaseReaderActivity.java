package com.pieter.declercq.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.pieter.declercq.datevalidator.domain.Category;
import com.pieter.declercq.datevalidator.domain.ExpiryProduct;
import com.pieter.declercq.datevalidator.domain.Product;
import com.pieter.declercq.datevalidator.exception.domain.DomainException;
import com.pieter.declercq.datevalidator.exception.service.ServiceException;
import com.pieter.declercq.datevalidator.service.DateValidator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/Proxy/Datumcontrole/ExpiryList.txt");
        boolean success;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }

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


        Log.d("ABC", "" + mDateValidator.getNumberOfExpiryProducts() );
        Log.d("ABC", "" + mDateValidator.getNumberOfProducts() );

        Intent i = new Intent(this, MainMenuActivity.class);
        startActivity(i);
        DatabaseReaderActivity.this.finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        writeExpiryList();
    }

    public void readProductDatabase(){
        try {
            reader = new BufferedReader(new FileReader(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/Proxy/Datumcontrole/ProductDatabase.txt")));
            //reader = new BufferedReader(new InputStreamReader(getAssets().open(databaseFileName)));
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
            reader = new BufferedReader(new FileReader(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/Proxy/Datumcontrole/ExpiryList.txt")));
            //reader = new BufferedReader(new InputStreamReader(getAssets().open(expiryListFileName)));
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
                ExpiryProduct ep = new ExpiryProduct(p, day, month, year, spot, removed);
                mDateValidator.addExpiryProduct(ep);

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

    public void writeProductDatabase(){
        try{
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/Proxy/Datumcontrole/ProductDatabase.txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            String ean = "";
            String name = "";
            String hope = "";
            String category = "";

            for(Product p : mDateValidator.getAllProducts()){
                String content = "";
                ean = String.valueOf(p.getEan());
                name = p.getName();
                hope = String.valueOf(p.getHope());
                category = p.getCategory().getName();

                content += ean;
                content += "\t";
                content += name;
                content += "\t";
                content += hope;
                content += "\t";
                content += category;

                bw.write(content);
                bw.newLine();
            }


            bw.close();

        } catch (Exception e){

        }
    }

    public void writeExpiryList(){
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/Proxy/Datumcontrole/ExpiryList.txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            SimpleDateFormat sdf = null;
            Date expiryDate = null;

            String category = "";
            String ean = "";
            String day = "";
            String month = "";
            String year = "";
            String spot = "";
            String removed = "";

            for(ExpiryProduct ep : mDateValidator.getAllExpiryProducts()){
                String content = "";
                category = ep.getCategory().getName();
                ean = String.valueOf(ep.getArticle().getEan());
                expiryDate = ep.getExpiryDate();
                sdf = new SimpleDateFormat("dd");
                day = sdf.format(expiryDate);
                sdf = new SimpleDateFormat("MM");
                month = sdf.format(expiryDate);
                sdf = new SimpleDateFormat("yyyy");
                year = sdf.format(expiryDate);
                spot = String.valueOf(ep.getSpot());
                removed = String.valueOf(ep.isRemoved());
                content += category;
                content += "\t";
                content += ean;
                content += "\t";
                content += day;
                content += "\t";
                content += month;
                content += "\t";
                content += year;
                content += "\t";
                content += spot;
                content += "\t";
                content += removed;

                bw.write(content);
                bw.newLine();
            }

            bw.close();

        } catch (Exception e) {}

    }

}