package com.example.admin.data.examEamcet;

import android.graphics.Color;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.data.R;
import com.example.admin.data.examEamcet.adapter.BranchAdapter;
import com.example.admin.data.examEamcet.model.Branches;
import com.example.admin.data.examEamcet.rest.ApiClient;
import com.example.admin.data.examEamcet.rest.ApiInterface;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Eamcet_Branches extends AppCompatActivity {

    private final static String API_KEY = "VNNk2xmBYia8LLhNcaUAQNckrMlXiLCI";
    Call<List<Branches>> call;
    List<Branches> bran;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eamcet__branches);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        start();
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
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
            Collections.sort(bran);
            BranchAdapter adapter = new BranchAdapter(bran, R.layout.recycle_branches, getApplicationContext());
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
            snackbar.show();
            // Toast.makeText(Eamcet_Branches.this, "No Internet Connection", Toast.LENGTH_LONG).show();

        }
    });
}catch (Exception e){
    start();
}
    }
}
