/* This Activity displays the price history of Petrol between 2001 and 2021 as a line graph.
 * Author: Sean Coll
 * Date Created: 26/02/22
 * Last Modified: 27/02/22
 */

package ie.tudublin.carml;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class PetrolActivity extends Activity implements View.OnClickListener {

    ImageButton back_arrow;
    LineChart petrolChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petrol);
        setUpViews();
        setUpChart();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case(R.id.back_arrow): {
                finish();
                break;
            }
        }
    }

    public void setUpViews() {
        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(this);
    }

    public void setUpChart() {
        // Set up and configure the chart
        petrolChart = findViewById(R.id.petrol_chart);
        petrolChart.setDragEnabled(true);
        petrolChart.setScaleEnabled(true);
        petrolChart.setBackgroundColor(Color.WHITE);
        petrolChart.getAxisRight().setEnabled(false);
        petrolChart.setDrawGridBackground(false);
        petrolChart.getDescription().setEnabled(false);
        petrolChart.getLegend().setEnabled(false);

        // Values for x axis
        ArrayList<String> xVals = new ArrayList<>();
        //Values for y axis
        ArrayList<Entry> yVals = new ArrayList<>();

        // Get InputStream for petrol data
        InputStream inStream = getResources().openRawResource(R.raw.petrol_prices);
        // Create Buffered Reader for data stream
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inStream, StandardCharsets.UTF_8));

        String row;
        String[] rowSplit;
        int i = 0;
        // Read each row and check if it is the same as the user's query
        // Each row consist of Month,Price
        try {
            // Skip over the column headers
            reader.readLine();
            // While there is a row to read
            while((row = reader.readLine()) != null)
            {
                rowSplit = row.split(",");
                xVals.add(rowSplit[0]);
                yVals.add(new Entry(i, Float.parseFloat(rowSplit[1])));
                i++;
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

        // Create the data set for the y axis
        LineDataSet ySet = new LineDataSet(yVals, "Price in cent");
        ySet.setFillAlpha(100);
        ySet.setColor(Color.BLACK);
        ySet.setLineWidth(1f);
        ySet.setDrawValues(false);
        ySet.setDrawCircles(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(ySet);

        // Assign the data to the chart
        LineData data = new LineData(dataSets);
        petrolChart.setData(data);

        // Configure the x axis
        XAxis xAxis = petrolChart.getXAxis();
        String[] xValsArray = new String[xVals.size()];
        xValsArray = xVals.toArray(xValsArray);
        xAxis.setValueFormatter(new XAxisStringFormatter(xValsArray));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        // Configure the x axis
        YAxis yAxis = petrolChart.getAxisLeft();
        yAxis.setDrawLabels(true);
        yAxis.setCenterAxisLabels(true);
    }
}

