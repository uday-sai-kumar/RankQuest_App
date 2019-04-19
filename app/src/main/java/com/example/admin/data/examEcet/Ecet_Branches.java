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
import android.widget.Toast;

import com.example.admin.data.R;
import com.example.admin.data.examEcet.adapter.BranchAdapter;
import com.example.admin.data.examEcet.model.Branch;
import com.example.admin.data.examEcet.rest.ApiClient;
import com.example.admin.data.examEcet.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ecet_Branches extends AppCompatActivity {

    private final static String API_KEY = "VNNk2xmBYia8LLhNcaUAQNckrMlXiLCI";
    List<Branch> list;
    Call<List<Branch>> call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecet__branches);
        start();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

            final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclebranch);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(this, R.anim.layout_leftto_right);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            call = apiService.getData(API_KEY);
            call.enqueue(new Callback<List<Branch>>() {
                @Override
                public void onResponse(Call<List<Branch>> call, Response<List<Branch>> response) {

                    list = response.body();

                    alert.dismiss();
                    BranchAdapter adapter = new BranchAdapter(list, R.layout.recycle_branches, getApplicationContext());
                    recyclerView.setAdapter(adapter);

                    recyclerView.setLayoutAnimation(controller);
                    adapter.notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();
                    //Toast.makeText(getApplicationContext(), "Succesfully Loaded", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<List<Branch>> call, Throwable t) {

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
        }
        catch (Exception e){
            start();
        }
    }
}
