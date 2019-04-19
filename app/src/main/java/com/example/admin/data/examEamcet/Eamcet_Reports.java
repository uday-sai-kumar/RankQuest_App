package com.example.admin.data.examEamcet;

import android.app.Dialog;
import android.content.Context;
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
import com.example.admin.data.examEamcet.rest.ApiClient;
import com.example.admin.data.examEamcet.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Eamcet_Reports extends AppCompatActivity {
    private final static String API_KEY = "VNNk2xmBYia8LLhNcaUAQNckrMlXiLCI";
    ReportingCentersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eamcet__reports);

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

            final RecyclerView r = (RecyclerView) findViewById(R.id.recyclereports);
            r.setLayoutManager(new LinearLayoutManager(this));

            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(this, R.anim.layout_leftto_right);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            Call<List<ReportingCenters>> call = apiService.getCentre(API_KEY);
            call.enqueue(new Callback<List<ReportingCenters>>() {
                @Override
                public void onResponse(Call<List<ReportingCenters>> call, Response<List<ReportingCenters>> response) {
                    List<ReportingCenters> colg = response.body();


                    alert.dismiss();
                    // Toast.makeText(Eamcet_Reports.this, "Done", Toast.LENGTH_SHORT).show();
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
                    //Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e){
            start();
        }
    }

}
