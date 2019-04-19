package com.example.admin.data.examPgecet;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.admin.data.R;

public class Pgcet_Checklist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pgcet__checklist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
