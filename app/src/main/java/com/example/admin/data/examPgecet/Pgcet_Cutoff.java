package com.example.admin.data.examPgecet;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.data.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class Pgcet_Cutoff extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    MaterialCardView show, branches, colleges;
    EditText rank;
    RadioGroup gg,g;
    Spinner cast, councling;
    RadioButton male,female;
    static  String t1,gender="M",sortby="yes";
    int t2;
    static StringBuilder stringBuilder;
    static String item,item2,q;
    static int requestCode1=1,requestCode2=2;
    String[] d1={},d2={};
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pgcet__cutoff);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rank = (EditText) findViewById(R.id.rank);
        show =  findViewById(R.id.show);
        gg = (RadioGroup) findViewById(R.id.gg);
        cast = (Spinner) findViewById(R.id.cast);
        councling = (Spinner) findViewById(R.id.counselling);
        branches =  findViewById(R.id.branches);
        colleges =  findViewById(R.id.colleges);
        g=findViewById(R.id.g);

        g.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.college){
                    sortby="yes";
                }
                else{
                    sortby="no";
                }
            }
        });
        gg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.male){
                    gender="M";
                }
                else{
                    gender="F";
                }
            }
        });

        cast.setOnItemSelectedListener(this);
        councling.setOnItemSelectedListener(this);
        List<String> castl = new ArrayList<String>();
        castl.add("OC");
        castl.add("BC_A");
        castl.add("BC_B");
        castl.add("BC_C");
        castl.add("BC_D");
        castl.add("BC_E");
        castl.add("SC");
        castl.add("ST");
        ArrayAdapter<String> castlist = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, castl);
        castlist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cast.setAdapter(castlist);

        List<String> coucillist = new ArrayList<String>();
        coucillist.add("counselling 1");
        ArrayAdapter<String> counada = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, coucillist);
        counada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        councling.setAdapter(counada);

        branches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t=new Intent(getApplicationContext(),SelectBranchesPgcet.class);
                startActivityForResult(t,requestCode1);
            }
        });

        colleges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Colleges", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),Cutoff_Select_Colleges_Pgcet.class);
                startActivityForResult(i,requestCode2);
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1=rank.getText().toString();
                if (t1.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please provide Rank", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(t1)==0){
                    Toast.makeText(getApplicationContext(), "Please provide valid rank", Toast.LENGTH_SHORT).show();

                }
                else if(d1.length<=0){
                    Toast.makeText(getApplicationContext(), "Please select Branches", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = new Intent(getApplicationContext(), Pgcet_Cutoff_Result.class);
                    i.putExtra("gender",gender);
                    i.putExtra("caste",item);
                    i.putExtra("council",item2);
                    i.putExtra("rank",t1);
                    i.putExtra("sortby",sortby);
                    Bundle b = new Bundle();
                    b.putStringArray("selectedItems",d1);
                    i.putExtras(b);
                    Bundle t=new Bundle();
                    t.putStringArray("selectedColleges",d2);
                    i.putExtras(t);
                    startActivity(i);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            d1 =data.getStringArrayExtra("selectedItems");
            for (int i=0;i<d1.length;i++){
                System.out.println(d1[i]);
            }
        }
        if(resultCode == 2){
            d2=data.getStringArrayExtra("selectedColleges");

            for (int i=0;i<d2.length;i++){
                System.out.println(d2[i]);
            }
        }

    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.cast:
                item=adapterView.getItemAtPosition(i).toString();    //caste
                // Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
            case R.id.counselling:
                item2=adapterView.getItemAtPosition(i).toString();  //counselling
                //Toast.makeText(this, item2, Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        item="OC";
        item2="counselling 1";
    }
}
