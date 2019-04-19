package com.example.admin.data.examPgecet;

import android.graphics.Color;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.data.R;
import com.example.admin.data.examPgecet.adapter.BranchAdapter;
import com.example.admin.data.examPgecet.model.Branches;
import com.example.admin.data.examPgecet.rest.ApiClient;
import com.example.admin.data.examPgecet.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Pgcet_Branches extends AppCompatActivity {
    private final static String API_KEY = "VNNk2xmBYia8LLhNcaUAQNckrMlXiLCI";
    Call<List<Branches>> call;
    List<Branches> bran;
    BranchAdapter adapter;
    EditText t;


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pgcet__branches);
        start();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        t = findViewById(R.id.branch);
        t.setVisibility(View.INVISIBLE);
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
            final RecyclerView recyclerView = findViewById(R.id.recyclebranch);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(this, R.anim.layout_leftto_right);

            call = apiService.getBran(API_KEY);
            call.enqueue(new Callback<List<Branches>>() {
                @Override
                public void onResponse(Call<List<Branches>> call, Response<List<Branches>> response) {
                    bran = response.body();

                    alert.dismiss();
                    t.setVisibility(View.VISIBLE);
                    adapter = new BranchAdapter(bran, R.layout.recycle_branches, getApplicationContext());
                    recyclerView.setAdapter(adapter);

                    recyclerView.setLayoutAnimation(controller);
                    adapter.notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();
                    //Toast.makeText(Eamcet_Branches.this, "Succesfully Loaded", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<List<Branches>> call, Throwable t) {

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
                    snackbar.show();//Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            });
            setupsearch();
        }
        catch (Exception e){

        }
    }
    public void setupsearch(){
        t=findViewById(R.id.branch);
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
        List<Branches> filterdNames =new ArrayList<>();

        //looping through existing elements
        for (Branches s : bran) {
            //if the existing elements contains the search input
            if (s.getName().contains(text.toUpperCase())||s.getBranch().contains(text.toUpperCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }
        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames);

    }
}
