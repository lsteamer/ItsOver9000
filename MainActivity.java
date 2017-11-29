package com.example.android.itsover9000;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.itsover9000.model.Feed;
import com.example.android.itsover9000.model.Values;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //A progress bar to help the design
    private ProgressBar pbLoadingIndicator;

    //The Line Chart inside the layout
    private LineChart lcBitCoinChart;

    //Base URL used to access the API
    private static final String BASE_URL = "https://api.blockchain.info/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Finding the Line Chart
        lcBitCoinChart = findViewById(R.id.lcMainChart);


        //Finding the Loading indicator
        pbLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        //Creating the retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Creating the bitCoinAPI interface
        final BitCoinAPI bitCoinAPI = retrofit.create(BitCoinAPI.class);

        //Creating a call object
        Call<Feed> call = bitCoinAPI.getData();

        //Adding an enqueue to our call
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {

                //Getting the values
                ArrayList<Values> valuesArrayList = response.body().getValues();

                /*Calling the lineCreator that provides the correct LineData

                 */
                LineData data = lineCreator(valuesArrayList, "BitCoin "+response.body().getName());

                //setting our existing LineChart with our LineData
                lcBitCoinChart.setData(data);

                //lcBitCoinChart.setVisibleYRangeMaximum(0.1f, YAxis.AxisDependency.LEFT);
                lcBitCoinChart.setVisibleXRangeMaximum(1500);
                lcBitCoinChart.setScaleYEnabled(false);
                invertScreenVisibility();

            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {


                invertScreenVisibility();

            }
        });

    }


    //This method receives the ArrayList<Values> and gives back the LineData with the correct information
    protected LineData lineCreator(ArrayList<Values> arrayListValues, String label){

        //Creating an array or Entry to hold the yValue
        ArrayList<Entry> yValue = new ArrayList<>();

        //Cycle through the existing array
        for(int i=0; i<arrayListValues.size(); i++){
            //If the Value is 0 we don't take it
            if(arrayListValues.get(i).getY()!="0")
                //We add to yValue a new Entry. the New entry takes two values "i" (For now, I want the date to be shown) and a Float representation of the String with the BitCoin
                yValue.add(new Entry(i,Float.parseFloat(arrayListValues.get(i).getY())));
        }

        //Creating the single Line. Adding the yValue and a label
        LineDataSet lineDataSet = new LineDataSet(yValue,label);

        //Setting parameters for the new single line
        lineDataSet.setDrawCircles(false);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setLineWidth(3f);

        ArrayList<ILineDataSet> dataSet = new ArrayList<>();

        //Adding the Line to a data set
        dataSet.add(lineDataSet);

        //returning the data set
        return new LineData(dataSet);
    }

    //Small method that switches between the Progress Bar and the Line Chart
    protected void invertScreenVisibility(){
        if(lcBitCoinChart.getVisibility()==View.VISIBLE){
            lcBitCoinChart.setVisibility(View.INVISIBLE);
            pbLoadingIndicator.setVisibility(View.VISIBLE);
        }
        else{
            lcBitCoinChart.setVisibility(View.VISIBLE);
            pbLoadingIndicator.setVisibility(View.INVISIBLE);

        }
    }

}
