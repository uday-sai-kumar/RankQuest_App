package com.example.admin.data.examEcet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;

import com.example.admin.data.examEcet.model.Branch;
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
import com.example.admin.data.examEcet.adapter.StatasticsAdapter_Ecet;
import com.example.admin.data.examEcet.model.CollegeDetails;
import com.example.admin.data.examEcet.rest.ApiClient;
import com.example.admin.data.examEcet.rest.ApiInterface;
import com.example.admin.data.examPgecet.model.Exam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ecet_Statatics extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    ArrayList<String> labels = new ArrayList<String>();
    private final static String API_KEY = "VNNk2xmBYia8LLhNcaUAQNckrMlXiLCI";
    Call<List<CollegeDetails>> call;
    List<CollegeDetails> bran;
    List<Exam> examm,examf;
    Call<List<Exam>> call3,call4;
    Call<List<Branch>> call2;
    EditText t;
    List<Branch> branches;
    Spinner s;
    int male,female;
    String[] d;
    String branchselected;
    StatasticsAdapter_Ecet adapter;
    List<String> coucillist;
    ProgressDialog progress;
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecet__statatics);
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


            call2 = apiInterface1.getData(API_KEY);

            call2.enqueue(new Callback<List<Branch>>() {
                @Override
                public void onResponse(Call<List<Branch>> call, Response<List<Branch>> response) {
                    branches = response.body();
                    alert.dismiss();
                    if (branches.size() > 0) {
                        d = new String[branches.size()];
                        for (int i = 0; i < branches.size(); i++) {
                            if (branches.get(i).getBranch().equals("INF")) {

                                d[i] = "PET--PETROLEUM TECHNOLOGY";
                            } else {
                                d[i] = branches.get(i).getBranch()+"--"+branches.get(i).getName().toUpperCase();
                            }

                        }
                        if (d.length > 0) {
                            coucillist = Arrays.asList(d);
                            branchselected = coucillist.get(0).substring(0,3);
                            //Toast.makeText(this, "Branchselected  :"+branchselected, Toast.LENGTH_SHORT).show();
                            ArrayAdapter<String> counada = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, coucillist);
                            counada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            s.setAdapter(counada);

                            s.setOnItemSelectedListener(Ecet_Statatics.this);
                            s.setVisibility(View.VISIBLE);
                            t.setVisibility(View.VISIBLE);
                            //start(branchselected);
                        }
                    }


                }

                @Override
                public void onFailure(Call<List<Branch>> call, Throwable t) {
                    alert.dismiss();
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.ll), "No Network", Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getBranchesIntoSpinner();
                                }
                            }).setDuration(100000);
                    View snackbarView = snackbar.getView();
                    snackbarView.setMinimumHeight(25);
                    snackbarView.setBackgroundResource(R.color.orange);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_action);
                    textView.setTextColor(Color.BLACK);
                    TextView textView1 = snackbarView.findViewById(R.id.snackbar_text);
                    textView1.setTextSize(25);
                    textView.setTextSize(15);
                    snackbar.show();
                }
            });
        }
        catch (Exception e){
            getBranchesIntoSpinner();
        }
    }
    void start(String branchselected){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        AlertDialog alert = alertDialog.create();
        alert.show();
        alert.setCancelable(false);
        alert.setContentView(R.layout.loadingdialog);

        try {

            if (API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtain your API KEY", Toast.LENGTH_LONG).show();
                return;
            } else {
                final RecyclerView recyclerVie = (RecyclerView) findViewById(R.id.recyclestatastics);
                recyclerVie.setLayoutManager(new LinearLayoutManager(this));

                final LayoutAnimationController controller =
                        AnimationUtils.loadLayoutAnimation(this, R.anim.layout_leftto_right);

                String e = "{branch:'" + branchselected + "'}";
                Log.i("At College Display", "   : Done");
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

                call = apiService.getColleg(e);
                call.enqueue(new Callback<List<CollegeDetails>>() {
                    @Override
                    public void onResponse(Call<List<CollegeDetails>> call, Response<List<CollegeDetails>> response) {
                        bran = response.body();
                        Collections.sort(bran);
                        alert.dismiss();
                        HashMap<String, Integer> map = calculateIndexesForName(bran);
                        Log.i("At College Adapter", "   : Done");
                        adapter = new StatasticsAdapter_Ecet(bran,map, new StatasticsAdapter_Ecet.OnItemClick() {
                            @Override
                            public void onItem(CollegeDetails item) {
                                //  Toast.makeText(getApplicationContext(), "Selected College Code: "+item.getCode(), Toast.LENGTH_SHORT).show();
                                alert.show();
                                ApiInterface apiInterface3 = ApiClient.getClient().create(ApiInterface.class);
                                Log.i("At College Selection", "   : Done");

                                String m = "{branch:'" + branchselected + "',code:'" + item.getCode() + "',sex:'M'}";
                                call3 = apiInterface3.getExam(m);

                                call3.enqueue(new Callback<List<Exam>>() {
                                    @Override
                                    public void onResponse(Call<List<Exam>> call, Response<List<Exam>> response) {
                                        examm = response.body();

                                        male = examm.size();
                                        String f = "{branch:'" + branchselected + "',code:'" + item.getCode() + "',sex:'F'}";
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
    private HashMap<String, Integer> calculateIndexesForName(List<CollegeDetails> c){

        HashMap<String, Integer> mapIndex = new LinkedHashMap<>();
        for (int i = 0; i<c.size(); i++){
            String name = c.get(i).getName();
            System.out.println(name+"\n");
            String index = name.substring(0,1);
            System.out.println(index+"hello");
            index = index.toUpperCase();
            System.out.println("what in upper"+index);

            if (!mapIndex.containsKey(index)) {
                mapIndex.put(index, i);
            }
        }
        System.out.println(mapIndex+"in map index");
        return mapIndex;
    }
}
