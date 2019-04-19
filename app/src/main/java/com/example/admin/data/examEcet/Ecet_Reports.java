package com.example.admin.data.examEcet;

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

import com.example.admin.data.R;
import com.example.admin.data.examEamcet.adapter.ReportingCentersAdapter;
import com.example.admin.data.examEamcet.model.ReportingCenters;
import com.example.admin.data.examEcet.rest.ApiClient;
import com.example.admin.data.examEcet.rest.ApiInterface;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ecet_Reports extends AppCompatActivity {
    private final static String API_KEY = "VNNk2xmBYia8LLhNcaUAQNckrMlXiLCI";
    ReportingCentersAdapter adapter;
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecet__reports);
        start();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
public void start(){
        try {
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            AlertDialog alert = alertDialog.create();
            alert.show();
            alert.setCancelable(false);
            alert.setContentView(R.layout.loadingdialog);

            final RecyclerView r = (RecyclerView) findViewById(R.id.recyclereports);
            r.setLayoutManager(new LinearLayoutManager(this));

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(this, R.anim.layout_leftto_right);
            Call<List<ReportingCenters>> call = apiService.getCentre(API_KEY);
            call.enqueue(new Callback<List<ReportingCenters>>() {
                @Override
                public void onResponse(Call<List<ReportingCenters>> call, Response<List<ReportingCenters>> response) {
                    List<ReportingCenters> colg = response.body();
                    alert.dismiss();
                    adapter = new ReportingCentersAdapter(colg, getApplicationContext());
                    r.setAdapter(adapter);

                    r.setLayoutAnimation(controller);
                    adapter.notifyDataSetChanged();
                    r.scheduleLayoutAnimation();

                }

                @Override
                public void onFailure(Call<List<ReportingCenters>> call, Throwable t) {
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
                }
            });
        }catch (Exception e){
            start();
        }
    }
}
