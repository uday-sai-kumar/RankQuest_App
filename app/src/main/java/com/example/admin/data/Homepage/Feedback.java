package com.example.admin.data.Homepage;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.admin.data.Homepage.rest.ApiInterfacePut;
import com.example.admin.data.Homepage.rest.Register_Client;
import com.example.admin.data.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Feedback extends AppCompatActivity {
    public String b,r,e;
    public float t;
    SimpleDateFormat formatter;
    Date date;
     RatingBar ratingbar;
    Button button;
    TextInputEditText bug;
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
         bug =  findViewById(R.id.bug);



        MobileAds.initialize(this,"ca-app-pub-1002768150287472~3177562999");
        AdView adview=findViewById(R.id.adViewEamcet);
        AdRequest adRequest=new AdRequest.Builder().build();
        adview.loadAd(adRequest);


        ratingbar =(RatingBar) findViewById(R.id.ratingBar);
        button =(Button) findViewById(R.id.button);

         formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        date = new Date();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                b=bug.getText().toString();

                t=ratingbar.getRating();
                if(b.equals("")){
                    Toast.makeText(getApplicationContext(), "Please Enter Something", Toast.LENGTH_SHORT).show();
                }
                else {
                    button.setVisibility(View.INVISIBLE);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("bug", b);
                        jsonObject.put("rating", t);
                        jsonObject.put("date", formatter.format(date));
                        ApiInterfacePut apiPut = Register_Client.getService().create(ApiInterfacePut.class);
                        Call<ResponseBody> body1 = apiPut.feedback(jsonObject.toString());
                        body1.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(getApplicationContext(), "FeedBack Successfull", Toast.LENGTH_SHORT).show();
                                button.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "please check your network", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
