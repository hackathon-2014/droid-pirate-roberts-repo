package com.hackathon4.mash.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hackathon4.mash.Category;
import com.hackathon4.mash.MyCategoryView;
import com.hackathon4.mash.CategoryManager;
import com.hackathon4.mash.R;

public class ActivityEditCategory extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        final String category = getIntent().getStringExtra("category");
        TextView header = (TextView) findViewById(R.id.hdrCategory);
        header.setText(category);

        final EditText field1 = (EditText) findViewById(R.id.categoryField1);
        final EditText field2 = (EditText) findViewById(R.id.categoryField2);
        final EditText field3 = (EditText) findViewById(R.id.categoryField3);
        final EditText field4 = (EditText) findViewById(R.id.categoryField4);

        Button saveButton = (Button) findViewById(R.id.saveCategory);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category category1 = new Category();
                Category category2 = new Category();
                Category category3 = new Category();
                Category category4 = new Category();
                category1.addItem(field1.getText().toString());
                category2.addItem(field2.getText().toString());
                category3.addItem(field3.getText().toString());
                category4.addItem(field4.getText().toString());

                CategoryManager categoryManager = CategoryManager.getInstance();

                if (category.equals("Boys")) {
                    categoryManager.addCategory(category1);
                }
                if (category.equals("Boys")) {
                    categoryManager.addCategory(category2);
                }
                if (category.equals("Boys")) {
                    categoryManager.addCategory(category3);
                }
                if (category.equals("Boys")) {
                    categoryManager.addCategory(category4);
                }
                Intent intent = new Intent(v.getContext(), ActivityMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_edit_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
