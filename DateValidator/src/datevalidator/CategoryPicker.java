package datevalidator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import datevalidator.domain.Category;
import datevalidator.service.DateValidator;

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
    }

    public void populateListView(){
        mCategories = mDateValidator.getCategories();
        ArrayAdapter<Category> adapter = new CategoryAdapter();
        GridView list = (GridView) findViewById(R.id.category_buttons_listView);
        list.setAdapter(adapter);
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