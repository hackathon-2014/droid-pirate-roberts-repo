package com.hackathon4.mash.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hackathon4.mash.MyCategoryView;
import com.hackathon4.mash.R;

public class ActivityMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateView(R.id.categoryNw, "Crushes", new String[]{"Bob", "Tom", "Henry", "Joe"});
        populateView(R.id.categorySw, "Job", new String[]{"Doctor", "Nurse", "Janitor", "Hobo"});
        populateView(R.id.categoryNe, "Car", new String[]{"Lamborghini", "Lexus", "Focus", "Gremlin"});
        populateView(R.id.categorySe, "City", new String[]{"Paris", "Charleston", "Albequequee", "North Pole"});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void populateView(int categoryId, String category, String[] items){
        MyCategoryView view = (MyCategoryView) findViewById(categoryId);

        view.setCategory(category);
        view.addListItems(items);
    }


}
