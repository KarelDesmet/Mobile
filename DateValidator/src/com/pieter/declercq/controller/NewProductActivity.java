package com.pieter.declercq.controller;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pieter.declercq.datevalidator.R;
import com.pieter.declercq.datevalidator.domain.Category;
import com.pieter.declercq.datevalidator.domain.ExpiryProduct;
import com.pieter.declercq.datevalidator.domain.Product;
import com.pieter.declercq.datevalidator.exception.domain.DomainException;
import com.pieter.declercq.datevalidator.exception.service.ServiceException;
import com.pieter.declercq.datevalidator.service.DateValidator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewProductActivity extends Activity {

    public static final String STATE_CATEGORYNAME = "com.pieter.declercq.datevalidator.category_name";

    private DateValidator mDateValidator;
    private Category mCategory;
    private Product mArticle;
    private EditText nameEditText, hopeEditText, eanEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        try {
            mDateValidator = new DateValidator();
        } catch (Exception e) {        }
        for (int i = 0; i < 7; i++) {
            mDateValidator.tomorrow();
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                try {
                    mCategory = new Category("default", 0);
                } catch (DomainException e) {
                }
            } else {
                String name = extras.getString(CategoryPickerActivity.CATEGORY_NAME);
                try {
                    mCategory = mDateValidator.getCategory(new Category(name));
                } catch (Exception e) {
                }

            }
        }

        try {
            if (mCategory == null) {
                mCategory = mDateValidator.getCategory(new Category(savedInstanceState.getString(STATE_CATEGORYNAME)));
            }
        } catch (Exception e){

        }

        TextView mCategoryBar = (TextView) findViewById(R.id.new_list_category);
        mCategoryBar.setBackgroundColor(mCategory.getmColor());
        String name = mCategory.getName();
        name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        mCategoryBar.setText(name);
        setUpDate();

        nameEditText = (EditText) findViewById(R.id.name_input);
        hopeEditText = (EditText) findViewById(R.id.hope_input);
        eanEditText = (EditText) findViewById(R.id.barcode_input);

        TextView fridge = (TextView) findViewById(R.id.fridge);
        try{fridge.setText(getResources().getText(R.string.frigo) + " " + (mDateValidator.getSpot(mCategory)+1));} catch (ServiceException e){}

        eanEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                final EditText eanEdit = (EditText) findViewById(R.id.barcode_input);
                Long ean = 0L;
                if (eanEdit.getText().length() != 0) {
                    ean = Long.parseLong(eanEdit.getText().toString());
                }

                try {
                    mArticle = mDateValidator.getProduct(mCategory, ean);
                    nameEditText.setText(mArticle.getName());
                    hopeEditText.setText("" + mArticle.getHope());
                } catch (ServiceException e) {
                    //do nothing
                }
            }
        });

        ImageButton previous_day = (ImageButton) findViewById(R.id.previous_day);
        previous_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateValidator.yesterday();
                setUpDate();
            }
        });
        ImageButton next_day = (ImageButton) findViewById(R.id.next_day);
        next_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateValidator.tomorrow();
                setUpDate();
            }
        });

        ImageButton previous_month = (ImageButton) findViewById(R.id.previous_month);
        previous_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateValidator.previousMonth();
                setUpDate();
            }
        });

        ImageButton next_month = (ImageButton) findViewById(R.id.next_month);
        next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateValidator.nextMonth();
                setUpDate();
            }
        });

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correctFields = true;
                final EditText eanEdit = (EditText) findViewById(R.id.barcode_input);
                final EditText nameEdit = (EditText) findViewById(R.id.name_input);
                final EditText hopeEdit = (EditText) findViewById(R.id.hope_input);
                final EditText dayEdit = (EditText) findViewById(R.id.day);
                final EditText monthEdit = (EditText) findViewById(R.id.month);
                final EditText yearEdit = (EditText) findViewById(R.id.year);
                if(eanEdit.getText().length() < 8){
                    correctFields = false;
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.err_short_ean), Toast.LENGTH_SHORT).show();
                }
                if(nameEdit.getText().length() < 5){
                    correctFields = false;
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.err_short_name), Toast.LENGTH_SHORT).show();
                }
                if(Integer.parseInt(nameEdit.getText().toString()) < 0){
                    correctFields = false;
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.err_negative_hope), Toast.LENGTH_SHORT).show();
                }
                Product p = null;
                if(correctFields) {
                    Long ean = Long.parseLong(eanEdit.getText().toString());
                    try {
                        p = mDateValidator.getProduct(mCategory, ean);
                        int day = Integer.parseInt(dayEdit.getText().toString());
                        int month = Integer.parseInt(monthEdit.getText().toString());
                        int year = Integer.parseInt(yearEdit.getText().toString());
                        int spot = mDateValidator.getSpot(mCategory);
                        ExpiryProduct ep = new ExpiryProduct(p, day, month, year, spot);
                        mDateValidator.addExpiryProduct(ep);
                        clear();
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.ep_add_succes), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        String name = nameEdit.getText().toString();
                        int hope = Integer.parseInt(hopeEdit.getText().toString());
                        try {
                            p = new Product(ean, name, hope, mCategory);
                            mDateValidator.addProduct(p);
                            int day = Integer.parseInt(dayEdit.getText().toString());
                            int month = Integer.parseInt(monthEdit.getText().toString());
                            int year = Integer.parseInt(yearEdit.getText().toString());
                            int spot = mDateValidator.getSpot(mCategory);
                            ExpiryProduct ep = new ExpiryProduct(p, day, month, year, spot);
                            mDateValidator.addExpiryProduct(ep);
                            clear();
                            Toast.makeText(getApplicationContext(), getResources().getText(R.string.ep_and_p_add_succes), Toast.LENGTH_SHORT).show();
                        } catch (Exception e2) {

                        }
                    }
                }
            }

        });

        ImageButton nextFridge = (ImageButton) findViewById(R.id.next_fridge);
        final ImageButton previousFridge = (ImageButton) findViewById(R.id.previous_fridge);
        //previousFridge.setEnabled(false);

        nextFridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mDateValidator.next(mCategory);
                    TextView fridge = (TextView) findViewById(R.id.fridge);
                    fridge.setText(getResources().getText(R.string.frigo) + " " + (mDateValidator.getSpot(mCategory)+1));
                } catch (ServiceException e){
                    //do nothing
                }
            }
        });

        previousFridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mDateValidator.previous(mCategory);
                    TextView fridge = (TextView) findViewById(R.id.fridge);
                    fridge.setText(getResources().getText(R.string.frigo) + " " + (mDateValidator.getSpot(mCategory)+1));
                } catch (ServiceException e){
                    //do nothing
                }
            }
        });

        Button mDeleteButton = (Button) findViewById(R.id.removeButton);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });
        }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putString(STATE_CATEGORYNAME, mCategory.getName());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        writeProductDatabase();
        writeExpiryList();
    }

    public void setUpDate() {
        Date today = mDateValidator.getToday();

        EditText day = (EditText) findViewById(R.id.day);
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        day.setText(sdf.format(today));

        EditText month = (EditText) findViewById(R.id.month);
        sdf = new SimpleDateFormat("MM");
        month.setText(sdf.format(today));

        EditText year = (EditText) findViewById(R.id.year);
        sdf = new SimpleDateFormat("yyyy");
        year.setText(sdf.format(today));

    }

    public void clear() {
        mDateValidator.today();
        eanEditText.setText("");
        nameEditText.setText("");
        hopeEditText.setText("");
        eanEditText.requestFocus();
        setUpDate();
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
