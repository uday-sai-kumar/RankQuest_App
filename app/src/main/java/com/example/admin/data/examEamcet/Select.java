package com.example.admin.data.examEamcet;

import android.content.Intent;
import android.content.res.Resources;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.admin.data.R;

import java.util.ArrayList;

public class Select extends AppCompatActivity {

    ArrayList<String> selectedItems;
    ArrayAdapter<String> adapter;
    ListView lv;
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        try {

            SparseBooleanArray checked = lv.getCheckedItemPositions();
            selectedItems = new ArrayList<String>();
            for (int i = 0; i < checked.size(); i++) {
                // Item position in adapter
                int position = checked.keyAt(i);
                // Add sport if it is checked i.e.) == TRUE!
                if (checked.valueAt(i))
                    selectedItems.add(adapter.getItem(position));
            }

            String[] outputStrArr = new String[selectedItems.size()];

            for (int i = 0; i < selectedItems.size(); i++) {
                outputStrArr[i] = selectedItems.get(i);
            }

            Intent intent = new Intent();
            Bundle b = new Bundle();
            b.putStringArray("selectedItems", outputStrArr);
            intent.putExtras(b);
            setResult(1,intent);
            finish();

        }catch (Exception e){

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);


        lv = (ListView) findViewById(R.id.list);
        Button ok=(Button)findViewById(R.id.ok);

        Resources r=getResources();
        String[] b=r.getStringArray(R.array.branch);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, b);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setAdapter(adapter);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray checked = lv.getCheckedItemPositions();
                selectedItems = new ArrayList<String>();
                for (int i = 0; i < checked.size(); i++) {
                    // Item position in adapter
                    int position = checked.keyAt(i);
                    // Add sport if it is checked i.e.) == TRUE!
                    if (checked.valueAt(i))
                        selectedItems.add(adapter.getItem(position));
                }

                String[] outputStrArr = new String[selectedItems.size()];

                for (int i = 0; i < selectedItems.size(); i++) {
                    outputStrArr[i] = selectedItems.get(i);
                }

                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putStringArray("selectedItems", outputStrArr);
                intent.putExtras(b);
                setResult(1,intent);
                finish();
            }
        });
    }
}
