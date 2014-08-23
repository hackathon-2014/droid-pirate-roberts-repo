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

import com.hackathon4.mash.MyCategoryView;
import com.hackathon4.mash.R;

public class ActivityEditCategory extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        String category = getIntent().getStringExtra("category");
        final Integer resultCode = getIntent().getIntExtra("resultCode", 1);
        TextView header = (TextView) findViewById(R.id.hdrCategory);
        header.setText(category);

        EditText field1 = (EditText) findViewById(R.id.categoryField1);
        EditText field2 = (EditText) findViewById(R.id.categoryField2);
        EditText field3 = (EditText) findViewById(R.id.categoryField3);
        EditText field4 = (EditText) findViewById(R.id.categoryField4);
        final String[] strings = new String[4];
        strings[0] = field1.getText().toString();
        strings[1] = field2.getText().toString();
        strings[2] = field3.getText().toString();
        strings[3] = field4.getText().toString();

        Button saveButton = (Button) findViewById(R.id.saveCategory);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putStringArray("strings", strings);
                intent.putExtras(bundle);
                setResult(resultCode,intent);
                startActivity(intent);
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
