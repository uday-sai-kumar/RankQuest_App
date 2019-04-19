package com.example.admin.data.examEamcet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.data.FastScroll.FastScrollRecyclerViewItemDecoration;
import com.example.admin.data.R;
import com.example.admin.data.examEamcet.adapter.CollegeAdapter;
import com.example.admin.data.examEamcet.model.College;
import com.example.admin.data.examEamcet.rest.ApiClient;
import com.example.admin.data.examEamcet.rest.ApiInterface;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Eamcet_Colleges extends AppCompatActivity {
    private final static String API_KEY = "VNNk2xmBYia8LLhNcaUAQNckrMlXiLCI";
    public final static String MESSAGE_KEY ="college_code";
    CollegeAdapter adapter;
    List<College> colg;
    EditText t;

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eamcet__colleges);
        t=(EditText)findViewById(R.id.searchview) ;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        t.setVisibility(View.INVISIBLE);
        start();

        SwipeRefreshLayout r;
        r=(SwipeRefreshLayout)findViewById(R.id.refreshcollege);
        r.setColorScheme(android.R.color.holo_orange_dark);
        r.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        start();
                        r.setRefreshing(false);
                    }
                },10000);
            }
        });


    }
    public void start(){

        try {

            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            AlertDialog alert = alertDialog.create();
            alert.show();
            alert.setCancelable(false);
            alert.setContentView(R.layout.loadingdialog);


            if (API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtain your API KEY", Toast.LENGTH_LONG).show();
                return;
            }
            final RecyclerView recyclerVie = (RecyclerView) findViewById(R.id.fastscroller);
            recyclerVie.setLayoutManager(new LinearLayoutManager(this));

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(this, R.anim.layout_leftto_right);
            Call<List<College>> call = apiService.getCollege(API_KEY);
            call.enqueue(new Callback<List<College>>() {
                @Override
                public void onResponse(Call<List<College>> call, Response<List<College>> response) {
                    colg = response.body();
                    t.setVisibility(View.VISIBLE);
                    alert.dismiss();
                    Collections.sort(colg);
                    HashMap<String,Integer> map=calculateIndexesForName(colg);
                    recyclerVie.setHasFixedSize(true);
                    //Toast.makeText(getApplicationContext(), "Colleges Found " + colg.size(), Toast.LENGTH_SHORT).show();
                    adapter = new CollegeAdapter(colg,map,R.layout.recyclecolleges, new CollegeAdapter.OnItemClick() {
                        @Override
                        public void onItem(College item) {
                            // Toast.makeText(Eamcet_Colleges.this, "Clicked "+item.getCode(), Toast.LENGTH_SHORT).show();
                            Intent r = new Intent(Eamcet_Colleges.this, Eamcet_Colleges_Details.class);
                            r.putExtra(MESSAGE_KEY, item.getCode());
                            startActivity(r);
                        }
                    }, getApplicationContext());
                    recyclerVie.setAdapter(adapter);
                    FastScrollRecyclerViewItemDecoration decoration = new FastScrollRecyclerViewItemDecoration(getApplicationContext());
                    recyclerVie.addItemDecoration(decoration);
                    recyclerVie.setItemAnimator(new DefaultItemAnimator());
                    recyclerVie.setLayoutAnimation(controller);
                    adapter.notifyDataSetChanged();
                    recyclerVie.scheduleLayoutAnimation();

                }

                @Override
                public void onFailure(Call<List<College>> call, Throwable t) {
                    alert.dismiss();
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.ll), "No Network", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    start();
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
                    // Toast.makeText(Eamcet_Colleges.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            });
            setupsearch();
        }
        catch (Exception e){
            start();
        }
    }
    public void setupsearch(){

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
        List<College> filterdNames =new ArrayList<College>();

        //looping through existing elements
        for (College s : colg) {
            //if the existing elements contains the search input
            if (s.getCollegename().contains(text.toUpperCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }
        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames);
    }
    private HashMap<String, Integer> calculateIndexesForName(List<College> items){

        HashMap<String, Integer> mapIndex = new LinkedHashMap<>();
        for (int i = 0; i<items.size(); i++){
            String name = items.get(i).getCollegename();
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
