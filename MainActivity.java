package com.example.android.itsover9000;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.itsover9000.model.Feed;
import com.example.android.itsover9000.model.Values;
import com.example.android.itsover9000.settings.SettingsActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //A progress bar to help the presentation
    private ProgressBar pbLoadingIndicator;

    //The Line Chart inside the layout
    private LineChart lcBitCoinChart;

    //Base URL used to access the API
    private static final String BASE_URL = "https://api.blockchain.info/";

    //String to hold the label for the line
    private String labelBitcoin;
    private static final String STRING_LABEL_PASS = "The indicator for the line";

    //ArrayList with the Entry values and It's constant to retrieve it from the Bundle
    private ArrayList<Entry> xValue;
    private static final String ENTRY_ARRAY_LIST_PASS = "The line value";

    //String Array with the Y-time values and it's constant to retrieve it from the Bundle
    private String [] datesArray;
    private static final String STRING_ARRAY_DATES_PASS = "The array of dates for the x-value";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Finding the Line Chart
        lcBitCoinChart = findViewById(R.id.lcMainChart);


        //Finding the Loading indicator
        pbLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        //Recovering data from a previous state
        if(savedInstanceState != null){
            datesArray = savedInstanceState.getStringArray(STRING_ARRAY_DATES_PASS);
            xValue = savedInstanceState.getParcelableArrayList(ENTRY_ARRAY_LIST_PASS);
            labelBitcoin = savedInstanceState.getString(STRING_LABEL_PASS);

            //Only if the information is valid should we run the lineCreator
            if(labelBitcoin!=null)
                lineCreator(xValue,datesArray,labelBitcoin);

        }
        //If there's no valid information from a previous state, Run this process
        if(labelBitcoin==null){
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

                    //The following line
                    labelBitcoin = "BitCoin "+response.body().getName();


                    //Array that will whole the Y-Axis labels
                    datesArray = new String[valuesArrayList.size()];

                    //Creating an array or Entry to hold the yValue
                    xValue = new ArrayList<>();

                    //Cycle through the existing array
                    for(int i=0; i<valuesArrayList.size(); i++){
                        //If the Value is 0 we don't take it
                        if(!((valuesArrayList.get(i).getY()).equals("0"))){
                            //We add to yValue a new Entry. the New entry takes two values "i" (For now, I want the date to be shown) and a Float representation of the String with the BitCoin
                            xValue.add(new Entry(i,Float.parseFloat(valuesArrayList.get(i).getY())));

                            //Getting the Date in a readable form
                            datesArray[i] = new SimpleDateFormat("dd/MMM/yy").format(valuesArrayList.get(i).getXData());
                        }
                    }

                    lineCreator(xValue,datesArray,labelBitcoin);
                }

                @Override
                public void onFailure(Call<Feed> call, Throwable t) {
                    invertScreenVisibility();
                }
            });

        }

    }


    //This method receives the ArrayList<Entries> and String[] to show in the screen the correct chart
    protected void lineCreator(ArrayList<Entry> arrayListEntryValues, String[] xLabels, String label){


        //Creating the single Line. Adding the yValue and a label
        LineDataSet lineDataSet = new LineDataSet(arrayListEntryValues, label);

        //Setting parameters for the new single line
        lineDataSet.setDrawCircles(false);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setLineWidth(3f);
        lineDataSet.setValueTextSize(5f);

        ArrayList<ILineDataSet> dataSet = new ArrayList<>();

        //Adding the Line to a data set
        dataSet.add(lineDataSet);

        //Creating the data set
        LineData data = new LineData(dataSet);


        //setting our existing LineChart with our LineData
        lcBitCoinChart.setData(data);


        //Setting parameters for the new Line Chart
        //lcBitCoinChart.setVisibleYRangeMaximum(0.1f, YAxis.AxisDependency.LEFT);
        lcBitCoinChart.setVisibleXRangeMaximum(1000);
        lcBitCoinChart.setScaleYEnabled(false);
        lcBitCoinChart.getDescription().setEnabled(false);

        //Placing the Day info in the X Axis
        XAxis xAxis = lcBitCoinChart.getXAxis();
        xAxis.setValueFormatter(new XAxisValueFormatter(xLabels));

        //Showing the chart and hiding the Loading Bar
        invertScreenVisibility();


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


    //Saving the Information to avoid Loading again.
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putStringArray(STRING_ARRAY_DATES_PASS,datesArray);
        outState.putParcelableArrayList(ENTRY_ARRAY_LIST_PASS,xValue);
        outState.putString(STRING_LABEL_PASS,labelBitcoin);
    }

    //Menu in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.visualizer_menu, menu);
        return true;
    }

    //Selecting items in the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings){
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Public class that binds the x-values with the given String Values
    public class XAxisValueFormatter implements IAxisValueFormatter{

        private String[] xValues;

        private XAxisValueFormatter(String[] values){
            this.xValues=values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return xValues[(int)value];
        }
    }

}
