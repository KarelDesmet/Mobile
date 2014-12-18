package com.pieter.declercq.datevalidator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.pieter.declercq.datevalidator.domain.Category;
import com.pieter.declercq.datevalidator.service.DateValidator;

import java.util.ArrayList;

/**
 * Created by Pieter on 17/12/2014.
 */
public class CategoryPicker extends Activity {

    private DateValidator mDateValidator;
    private ArrayList<Category> mCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_picker);

        mDateValidator = new DateValidator();

        populateListView();
        registerClickCallback();
    }

    public void populateListView(){
        mCategories = mDateValidator.getCategories();
        ArrayAdapter<Category> adapter = new CategoryAdapter();
        ListView list = (ListView) findViewById(R.id.category_buttons_listView);
        list.setAdapter(adapter);
    }

    public void registerClickCallback(){
            ListView list = (ListView) findViewById(R.id.category_buttons_listView);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View viewClicked,
                                        int position, long id) {

                    Category clickedCategory = mCategories.get(position);
                    String message = "You clicked position " + position
                            + " Which is " + clickedCategory.getName();
                    Toast.makeText(CategoryPicker.this, message, Toast.LENGTH_LONG).show();
                }
            });
    }

    private class CategoryAdapter extends ArrayAdapter<Category> {
        public CategoryAdapter(){
            super(CategoryPicker.this, R.layout.category_view, mCategories);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            View categoryView = convertView;
            if (categoryView == null){
                categoryView = getLayoutInflater().inflate(R.layout.category_view, parent, false);
            }

            final Category currentCategory = mCategories.get(position);

            ((Button) categoryView).setText(currentCategory.getName().toUpperCase());
            categoryView.setBackgroundColor(currentCategory.getColor());
            categoryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = "You clicked position " + position
                            + " Which is " + currentCategory.getName();
                    Toast.makeText(CategoryPicker.this, message, Toast.LENGTH_SHORT).show();
                }
            });

            return categoryView;
        }
    }
}