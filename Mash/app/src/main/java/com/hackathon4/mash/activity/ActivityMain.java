package com.hackathon4.mash.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hackathon4.mash.CategoryManager;
import com.hackathon4.mash.DrawView;
import com.hackathon4.mash.MyCategoryView;
import com.hackathon4.mash.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends Activity {

    private static final int CLEAR_DRAWING_MESSAGE = 0;

    private TextView tvMagicNumber;
    private DrawView drawView;
    List<List<TextView>> tvList = new ArrayList<List<TextView>>();

    Handler handler = new Handler() {
            /* (non-Javadoc)
             * @see android.os.Handler#handleMessage(android.os.Message)
             */
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CLEAR_DRAWING_MESSAGE) {
                drawView.clear();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyCategoryView categoryViewNw = (MyCategoryView) findViewById(R.id.categoryNw);
        Button catNwButton = (Button) categoryViewNw.findViewById(R.id.addToCategory);
        catNwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityEditCategory.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putString("category", "Boys");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        MyCategoryView categoryViewSw = (MyCategoryView) findViewById(R.id.categorySw);
        Button catSwButton = (Button) categoryViewSw.findViewById(R.id.addToCategory);
        catSwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityEditCategory.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putString("category", "Careers");
                intent.putExtras(bundle);
                startActivityForResult(intent, 2);
            }
        });

        MyCategoryView categoryViewNe = (MyCategoryView) findViewById(R.id.categoryNe);
        Button catNeButton = (Button) categoryViewNe.findViewById(R.id.addToCategory);
        catNeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityEditCategory.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putString("category", "Cities");
                intent.putExtras(bundle);
                startActivityForResult(intent, 3);
            }
        });

        MyCategoryView categoryViewSe = (MyCategoryView) findViewById(R.id.categorySe);
        Button catSeButton = (Button) categoryViewSe.findViewById(R.id.addToCategory);
        catSeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityEditCategory.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putString("category", "Kids");
                intent.putExtras(bundle);
                startActivityForResult(intent, 4);
            }
        });


        tvMagicNumber = (TextView) findViewById(R.id.magicNumber);

        final Button btnGo = (Button) findViewById(R.id.startDrawing);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGo.setVisibility(View.INVISIBLE);
                startDrawing();
            }
        });

        drawView = (DrawView) findViewById(R.id.drawview);
        drawView.setListener(new DrawView.DrawingListener() {
            @Override
            public void drawingStopped(int numberOfIntersections) {
                tvMagicNumber.setText(String.valueOf(numberOfIntersections));
                Message msg = handler.obtainMessage(CLEAR_DRAWING_MESSAGE);
                handler.sendMessageDelayed(msg, 1000);
            }
        });

        populateView(R.id.categoryNw, "Crushes", new String[]{"Bob", "Tom", "Henry", "Joe"});
        populateView(R.id.categorySw, "Job", new String[]{"Doctor", "Nurse", "Janitor", "Hobo"});
        populateView(R.id.categoryNe, "Car", new String[]{"Lamborghini", "Lexus", "Focus", "Gremlin"});
        populateView(R.id.categorySe, "City", new String[]{"Paris", "Charleston", "Albequequee", "North Pole"});

        calculateResults(5);
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
        tvList.add(view.addListItems(items));
    }

    private void startDrawing() {
        drawView.startDrawing();
    }

    private void calculateResults(int magicNumber) {
        int count = magicNumber;
        while (true){
            for (List<TextView> category: tvList){
                if (category.size() == 1){
                    continue;
                } else if (category.size() < count) {
                    count = count - category.size();
                } else if (category.size() == count){
                    //remove element
                    category.get(count-1).setPaintFlags(category.get(count-1).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    category.remove(count-1);
                    count=magicNumber;
                } else if (category.size() > count){
                    int priorSize = category.size();
                    category.get(count-1).setPaintFlags(category.get(count-1).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    int removed = count - 1;
                    category.remove(removed);
                    count = magicNumber - (priorSize - (removed + 1));
                } else {
                    throw new UnknownError();
                }
            }
            if (tvList.get(0).size()==1 &&
                    tvList.get(1).size()==1 &&
                    tvList.get(2).size()==1 &&
                    tvList.get(3).size()==1){
                break;
            }
        }

    }


}
