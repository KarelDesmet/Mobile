package com.pieter.declercq.datevalidator;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pieter.declercq.datevalidator.domain.Category;
import com.pieter.declercq.datevalidator.service.DateValidator;

import java.util.ArrayList;


public class ExpiryListActivity extends Activity {

    private DateValidator mDateValidator;
    private ArrayList<Category> mCategories;

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
            }
        });
        date.setText(mDateValidator.today());

        ImageButton tomorrow = (ImageButton) findViewById(R.id.tomorrow);
        tomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView date = (TextView) findViewById(R.id.day_of_the_week);
                date.setText(mDateValidator.tomorrow());
            }
        });

        ImageButton yesterday = (ImageButton) findViewById(R.id.yesterday);
        yesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView date = (TextView) findViewById(R.id.day_of_the_week);
                date.setText(mDateValidator.yesterday());
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

            categoryView.setBackgroundColor(currentCategory.getmColor());
            categoryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!currentCategory.ismSelected()) {
                        currentCategory.setmSelected(true);
                        ImageView triangle = (ImageView) catView.findViewById(R.id.triangle);
                        triangle.setImageResource(R.drawable.white_triangle);
                        name.setTextColor(Color.WHITE);
                        String message = "You clicked position " + position
                                + " Which is " + currentCategory.getName();
                        Toast.makeText(ExpiryListActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        currentCategory.setmSelected(false);
                        ImageView triangle = (ImageView) catView.findViewById(R.id.triangle);
                        triangle.setImageResource(R.drawable.black_triangle);
                        name.setTextColor(Color.BLACK);
                        String message = "You clicked position " + position
                                + " Which is " + currentCategory.getName();
                        Toast.makeText(ExpiryListActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return categoryView;
        }
    }
}
