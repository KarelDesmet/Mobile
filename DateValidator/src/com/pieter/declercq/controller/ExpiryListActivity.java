package com.pieter.declercq.controller;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pieter.declercq.datevalidator.R;
import com.pieter.declercq.datevalidator.domain.Category;
import com.pieter.declercq.datevalidator.domain.ExpiryProduct;
import com.pieter.declercq.datevalidator.exception.service.ServiceException;
import com.pieter.declercq.datevalidator.service.DateValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;


public class ExpiryListActivity extends Activity {

    private DateValidator mDateValidator;
    private ArrayList<Category> mCategories;
    private Map<Category, ArrayList<ExpiryProduct>> mExpiryProducts;
    private ArrayList<ExpiryProduct> mCategoryExpiryProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiry_list);

        try {
            mDateValidator = new DateValidator();
        } catch (Exception e){

        }
        setupDate();
        setupCategories();

    }


    public void setupDate(){
        TextView date = (TextView) findViewById(R.id.day_of_the_week);
        date.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                TextView date = (TextView) findViewById(R.id.day_of_the_week);
                date.setText(mDateValidator.today());
                try {
                    mExpiryProducts = mDateValidator.getExpiryProducts(mDateValidator.getToday());
                    setupCategories();
                } catch (ServiceException e) {
                    Log.d("PROXY", e.getMessage());
                }
            }
        });
        date.setText(mDateValidator.today());
        try {
            mExpiryProducts = mDateValidator.getExpiryProducts(mDateValidator.getToday());
            setupCategories();
        } catch (ServiceException e) {
            Log.d("PROXY", e.getMessage());
        }

        ImageButton tomorrow = (ImageButton) findViewById(R.id.tomorrow);
        tomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView date = (TextView) findViewById(R.id.day_of_the_week);
                date.setText(mDateValidator.tomorrow());
                try {
                    mExpiryProducts = mDateValidator.getExpiryProducts(mDateValidator.getToday());
                    setupCategories();
                } catch (ServiceException e) {
                    Log.d("PROXY", e.getMessage());
                }
            }
        });

        ImageButton yesterday = (ImageButton) findViewById(R.id.yesterday);
        yesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView date = (TextView) findViewById(R.id.day_of_the_week);
                date.setText(mDateValidator.yesterday());
                try {
                    mExpiryProducts = mDateValidator.getExpiryProducts(mDateValidator.getToday());
                    setupCategories();
                } catch (ServiceException e) {
                    Log.d("PROXY", e.getMessage());
                }
            }
        });

    }

    public void setupCategories(){
        mCategories = mDateValidator.getCategories();
        ArrayAdapter<Category> adapter = new CategoryAdapter();
        ListView list = (ListView) findViewById(R.id.expiryList_listView);
        list.setAdapter(adapter);
    }

    private class CategoryAdapter extends ArrayAdapter<Category> {
        public CategoryAdapter(){
            super(ExpiryListActivity.this, R.layout.advanced_category_view, mCategories);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            View categoryView = convertView;
            if (categoryView == null){
                categoryView = getLayoutInflater().inflate(R.layout.advanced_category_view, parent, false);
            }

            final Category currentCategory = mCategories.get(position);
            final View catView = categoryView;

            final TextView name = (TextView) categoryView.findViewById(R.id.category_names_textView);

            String catName = currentCategory.getName();
            catName = Character.toUpperCase(catName.charAt(0)) + catName.substring(1);
            name.setText(catName);

            LinearLayout categoryExpiryList = (LinearLayout) catView.findViewById(R.id.category_expiryList_listView);
            if(catView.findViewById(R.id.category_expiryList_listView) == null){
                categoryExpiryList = (LinearLayout) findViewById(R.id.category_expiryList_listView);
            }

            categoryExpiryList.setVisibility(View.GONE);

            categoryView.setBackgroundColor(currentCategory.getmColor());
            categoryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!currentCategory.ismSelected()) {
                        mCategoryExpiryProducts = mExpiryProducts.get(currentCategory);
                        currentCategory.setmSelected(true);
                        ImageView triangle = (ImageView) catView.findViewById(R.id.triangle);
                        triangle.setImageResource(R.drawable.white_triangle);
                        name.setTextColor(Color.WHITE);

                        LinearLayout categoryExpiryList = (LinearLayout) catView.findViewById(R.id.category_expiryList_listView);
                        if(catView.findViewById(R.id.category_expiryList_listView) == null){
                            categoryExpiryList = (LinearLayout) findViewById(R.id.category_expiryList_listView);
                        }
                        categoryExpiryList.setVisibility(View.VISIBLE);
                        //ArrayAdapter<ExpiryProduct> adapter = new ExpiryProductAdapter();
                        setUpExpiryProducts(categoryExpiryList);

                    } else {
                        currentCategory.setmSelected(false);
                        ImageView triangle = (ImageView) catView.findViewById(R.id.triangle);
                        triangle.setImageResource(R.drawable.black_triangle);
                        name.setTextColor(Color.BLACK);

                        LinearLayout categoryExpiryList = (LinearLayout) catView.findViewById(R.id.category_expiryList_listView);
                        if(catView.findViewById(R.id.category_expiryList_listView) == null){
                            categoryExpiryList = (LinearLayout) findViewById(R.id.category_expiryList_listView);
                        }
                        categoryExpiryList.setVisibility(View.GONE);
                    }
                }
            });

            return categoryView;
        }
    }

    public void setUpExpiryProducts(ViewGroup view) {
        if (view.getChildCount() <= mCategoryExpiryProducts.size()) {
            int previousSpot = 0;
            int currentSpot = 0;
            for (int i = 0; i < mCategoryExpiryProducts.size(); i++) {


                Collections.sort(mCategoryExpiryProducts, new Comparator<ExpiryProduct>() {
                    @Override
                    public int compare(ExpiryProduct ep1, ExpiryProduct ep2) {

                        return ep1.getSpot() - ep2.getSpot();
                    }
                });

                final ExpiryProduct ep = mCategoryExpiryProducts.get(i);

                currentSpot = mCategoryExpiryProducts.get(i).getSpot();
                //Log.d("ABC", "" + mCategoryExpiryProducts.get(i).getSpot());
                //Log.d("ABC", "" + (currentSpot != previousSpot));


                while(currentSpot != previousSpot){
                    View divider = (View) getLayoutInflater().inflate(R.layout.divider, view, false);
                    view.addView(divider);
                    previousSpot++;
                }

                View child = (View) getLayoutInflater().inflate(R.layout.expiry_product_view, view, false);

                final TextView name = (TextView) child.findViewById(R.id.expiry_product_name);
                name.setText(ep.getArticle().getName());
                if(ep.isRemoved()) {
                    name.setTextColor(Color.rgb(205, 192, 176));
                    name.setTypeface(null, Typeface.ITALIC);
                }
                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!ep.isRemoved()) {
                            Toast.makeText(ExpiryListActivity.this, getResources().getText(R.string.ep_hint) + " " + ep.getArticle().getHope(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                CheckBox removed = (CheckBox) child.findViewById(R.id.expiry_product_checkbox);
                removed.setChecked(ep.isRemoved());
                removed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            name.setTextColor(Color.rgb(205,192,176));
                            name.setTypeface(null, Typeface.ITALIC);
                            try { mDateValidator.remove(ep); } catch (ServiceException e) { Log.d("PROXY", e.getMessage()); }
                        } else {
                            name.setTextColor(Color.BLACK);
                            name.setTypeface(null, Typeface.NORMAL);
                            try { mDateValidator.cancelRemove(ep); } catch (ServiceException e) { Log.d("PROXY", e.getMessage()); }
                        }
                    }
                });


                view.addView(child);
                previousSpot = currentSpot;
            }
        }
    }

    private class ExpiryProductAdapter extends ArrayAdapter<ExpiryProduct> {
        public ExpiryProductAdapter() {
            super(ExpiryListActivity.this, R.layout.expiry_product_view, mCategoryExpiryProducts);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View expiryProductView = convertView;
            if (expiryProductView == null) {
                expiryProductView = getLayoutInflater().inflate(R.layout.expiry_product_view, parent, false);
            }
            Log.d("ABC", "" + mCategoryExpiryProducts.size());
            ExpiryProduct currentExpiryProduct = mCategoryExpiryProducts.get(position);

            ((TextView) expiryProductView).setText(currentExpiryProduct.getArticle().getName());

            return expiryProductView;
        }
    }
}
