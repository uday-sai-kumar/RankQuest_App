package com.example.admin.data.examPgecet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;

import com.example.admin.data.examPgecet.model.Branches;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.data.FastScroll.FastScrollRecyclerViewItemDecoration;
import com.example.admin.data.R;
import com.example.admin.data.examEamcet.Emcet_Statastics_Graphs;
import com.example.admin.data.examEamcet.adapter.StatasticsAdapter;
import com.example.admin.data.examPgecet.model.CollegeDetails;
import com.example.admin.data.examPgecet.model.Exam;
import com.example.admin.data.examPgecet.rest.ApiClient;
import com.example.admin.data.examPgecet.rest.ApiInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Pgcet_Statatics extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ArrayList<String> labels = new ArrayList<String>();
    private final static String API_KEY = "VNNk2xmBYia8LLhNcaUAQNckrMlXiLCI";
    Call<List<CollegeDetails>> call;
    List<CollegeDetails> bran;
    List<Exam> examm,examf;
    Call<List<Exam>> call3,call4;
     Call<List<Branches>> call2;
    EditText t;
    List<Branches> branches;
    Spinner s;
    int male,female;
    String[] d;
    String branchselected;
    StatasticsAdapter adapter;
    List<String> coucillist;
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pgcet__statatics);
        t=findViewById(R.id.statsearch);
        s=findViewById(R.id.statSpinner);
        s.setVisibility(View.INVISIBLE);
        t.setVisibility(View.INVISIBLE);
        getBranchesIntoSpinner();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public void getBranchesIntoSpinner()
    {

        try {
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            AlertDialog alert = alertDialog.create();
            alert.show();
            alert.setCancelable(false);
            alert.setContentView(R.layout.loadingdialog);

            ApiInterface apiInterface1 = ApiClient.getClient().create(ApiInterface.class);


            call2 = apiInterface1.getBran(API_KEY);

            call2.enqueue(new Callback<List<Branches>>() {
                @Override
                public void onResponse(Call<List<Branches>> call, Response<List<Branches>> response) {
                    branches = response.body();

                    alert.dismiss();
                    if (branches.size() > 0) {
                        d = new String[branches.size()];
                        for (int i = 0; i < branches.size(); i++) {
                            d[i] = branches.get(i).getBranch()+"--"+branches.get(i).getName().toUpperCase();
                        }
                        if (d.length > 0) {

                            for(int i = 0; i<d.length-1; i++) {
                                for (int j = i+1; j<d.length; j++) {
                                    if(d[i].compareTo(d[j])>0) {
                                        String temp = d[i];
                                        d[i] = d[j];
                                        d[j] = temp;
                                    }
                                }
                            }

                            coucillist = Arrays.asList(d);
                            branchselected = coucillist.get(0).substring(0,3);
                            //Toast.makeText(this, "Branchselected  :"+branchselected, Toast.LENGTH_SHORT).show();
                            ArrayAdapter<String> counada = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, coucillist);
                            counada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            s.setAdapter(counada);

                            s.setOnItemSelectedListener(Pgcet_Statatics.this);
                            s.setVisibility(View.VISIBLE);
                            t.setVisibility(View.VISIBLE);
                            //start(branchselected);
                        }
                    }


                }

                @Override
                public void onFailure(Call<List<Branches>> call, Throwable t) {
                    alert.dismiss();
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.ll), "No Network", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    start(branchselected);
                                }
                            });
                    View snackbarView = snackbar.getView();
                    snackbarView.setMinimumHeight(25);
                    snackbarView.setBackgroundResource(R.color.orange);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_action);
                    textView.setTextColor(Color.BLACK);
                    TextView textView1=snackbarView.findViewById(R.id.snackbar_text);
                    textView1.setTextSize(25);
                    textView.setTextSize(15);
                    snackbar.show();
                }
            });
        }catch (Exception e){
            getBranchesIntoSpinner();
        }
    }
    void start(String branchselected){

        try {
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            AlertDialog alert = alertDialog.create();
            alert.show();
            alert.setCancelable(false);
            alert.setContentView(R.layout.loadingdialog);

            if (API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtain your API KEY", Toast.LENGTH_LONG).show();
                return;
            } else {
                final RecyclerView recyclerVie = (RecyclerView) findViewById(R.id.recyclestatastics);
                recyclerVie.setLayoutManager(new LinearLayoutManager(this));

                String e = "{branch_code:{$regex:'" + branchselected + "'}}";

                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                final LayoutAnimationController controller =
                        AnimationUtils.loadLayoutAnimation(this, R.anim.layout_leftto_right);
                call = apiService.getStatatics(e);
                call.enqueue(new Callback<List<CollegeDetails>>() {
                    @Override
                    public void onResponse(Call<List<CollegeDetails>> call, Response<List<CollegeDetails>> response) {
                        bran = response.body();
                        Collections.sort(bran);
                        alert.dismiss();
                        HashMap<String,Integer> map=calculateIndexesForName(bran);
                        recyclerVie.setHasFixedSize(true);
                        adapter = new StatasticsAdapter(bran, map,new StatasticsAdapter.OnItemClick() {
                            @Override
                            public void onItem(CollegeDetails item) {
                                //  Toast.makeText(getApplicationContext(), "Selected College Code: "+item.getCode(), Toast.LENGTH_SHORT).show();
                                ApiInterface apiInterface3 = ApiClient.getClient().create(ApiInterface.class);
                                alert.show();

                                String m = "{branch:{$regex:'" + branchselected + "'},code:'" + item.getCode() + "',sex:'M'}";
                                call3 = apiInterface3.getExam(m);

                                call3.enqueue(new Callback<List<Exam>>() {
                                    @Override
                                    public void onResponse(Call<List<Exam>> call, Response<List<Exam>> response) {
                                        examm = response.body();

                                        male = examm.size();
                                        String f = "{branch:{$regex:'" + branchselected + "'},code:'" + item.getCode() + "',sex:'F'}";
                                        call4 = apiInterface3.getExam(f);
                                        call4.enqueue(new Callback<List<Exam>>() {
                                            @Override
                                            public void onResponse(Call<List<Exam>> call, Response<List<Exam>> response) {
                                                examf = response.body();
                                                alert.dismiss();

                                                female = examf.size();


                                                Intent i = new Intent(getApplicationContext(), Emcet_Statastics_Graphs.class);
                                                i.putExtra("male", male);
                                                i.putExtra("female", female);
                                                i.putExtra("name",item.getName());
                                                startActivity(i);
                                            }

                                            @Override
                                            public void onFailure(Call<List<Exam>> call, Throwable t) {
                                                alert.dismiss();
                                                Snackbar snackbar = Snackbar
                                                        .make(findViewById(R.id.ll), "No Network", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                start(branchselected);
                                                            }
                                                        });
                                                View snackbarView = snackbar.getView();
                                                snackbarView.setMinimumHeight(25);
                                                snackbarView.setBackgroundResource(R.color.orange);
                                                TextView textView = snackbarView.findViewById(R.id.snackbar_action);
                                                textView.setTextColor(Color.BLACK);
                                                TextView textView1=snackbarView.findViewById(R.id.snackbar_text);
                                                textView1.setTextSize(25);
                                                textView.setTextSize(15);
                                                snackbar.show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call<List<Exam>> call, Throwable t) {
                                        alert.dismiss();
                                        Snackbar snackbar = Snackbar
                                                .make(findViewById(R.id.ll), "No Network", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        start(branchselected);
                                                    }
                                                });
                                        View snackbarView = snackbar.getView();
                                        snackbarView.setMinimumHeight(25);
                                        snackbarView.setBackgroundResource(R.color.orange);
                                        TextView textView = snackbarView.findViewById(R.id.snackbar_action);
                                        textView.setTextColor(Color.BLACK);
                                        TextView textView1=snackbarView.findViewById(R.id.snackbar_text);
                                        textView1.setTextSize(25);
                                        textView.setTextSize(15);
                                        snackbar.show();
                                    }
                                });

                            }
                        }, getApplicationContext());
                        recyclerVie.setAdapter(adapter);
                        recyclerVie.setLayoutAnimation(controller);
                        adapter.notifyDataSetChanged();
                        recyclerVie.scheduleLayoutAnimation();
                        FastScrollRecyclerViewItemDecoration decoration = new FastScrollRecyclerViewItemDecoration(getApplicationContext());
                        recyclerVie.addItemDecoration(decoration);
                        recyclerVie.setItemAnimator(new DefaultItemAnimator());
                        setupsearch();

                    }

                    @Override
                    public void onFailure(Call<List<CollegeDetails>> call, Throwable t) {
                        alert.dismiss();
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.ll), "No Network", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        start(branchselected);
                                    }
                                });
                        View snackbarView = snackbar.getView();
                        snackbarView.setMinimumHeight(25);
                        snackbarView.setBackgroundResource(R.color.orange);
                        TextView textView = snackbarView.findViewById(R.id.snackbar_action);
                        textView.setTextColor(Color.BLACK);
                        TextView textView1=snackbarView.findViewById(R.id.snackbar_text);
                        textView1.setTextSize(25);
                        textView.setTextSize(15);
                        snackbar.show();
                    }
                });


            }
        }catch (Exception e){
            start(branchselected);
        }


    }
    public void setupsearch(){
        EditText t=findViewById(R.id.statsearch);
        t.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });
    }
    private void filter(String text) {
        //new array list that will hold the filtered data
        List<CollegeDetails> filterdNames =new ArrayList<CollegeDetails>();

        //looping through existing elements
        for (CollegeDetails s : bran) {
            //if the existing elements contains the search input
            if (s.getName().contains(text.toUpperCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }
        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        AlertDialog alert = alertDialog.create();
        alert.show();
        alert.setCancelable(false);
        alert.setContentView(R.layout.loadingdialog);

        branchselected=parent.getItemAtPosition(position).toString().substring(0,3);

       // Toast.makeText(getApplicationContext(), branchselected, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alert.dismiss();
                start(branchselected);
            }
        },5000);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private HashMap<String, Integer> calculateIndexesForName(List<CollegeDetails> items){

        HashMap<String, Integer> mapIndex = new LinkedHashMap<>();
        for (int i = 0; i<items.size(); i++){
            String name = items.get(i).getName();
            System.out.println(name+"\n");
            String index = name.substring(0,1);
            Log.i(index,"hello");
            index = index.toUpperCase();
            Log.i("what in upper",index);

            if (!mapIndex.containsKey(index)) {
                mapIndex.put(index, i);
            }
        }
        System.out.println(mapIndex+"in map index");
        return mapIndex;
    }
}
