package com.example.android.itsover9000;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.itsover9000.model.Feed;
import com.example.android.itsover9000.model.Values;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String BASE_URL = "https://api.blockchain.info/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BitCoinAPI bitCoinAPI = retrofit.create(BitCoinAPI.class);

        Call<Feed> call = bitCoinAPI.getData();

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                Log.d(TAG, "Message onResponse: " + response.body().getName());

                ArrayList<Values> valuesArrayList = response.body().getValues();

                //Checking that the last of the info is there
                Log.d(TAG, "Message on Response: \n"+
                        "FIRST: \n"+
                        "x: "+ valuesArrayList.get(0).getX() + "\n" +
                        "y: "+ valuesArrayList.get(0).getY()+"\n" +
                        "LAST: \n"+
                                "x: "+ valuesArrayList.get(valuesArrayList.size()-1).getX() + "\n" +
                                "y: "+ valuesArrayList.get(valuesArrayList.size()-1).getY()+"\n" +
                        "-----------------------------------------------------------------------------\n\n");

            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {


                Log.d(TAG, "Message onFailure: Something went wrong  " + t.getMessage());

            }
        });

    }
}
