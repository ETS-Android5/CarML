package ie.tudublin.carml;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
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

        petrolChart = findViewById(R.id.petrol_chart);
        petrolChart.setDragEnabled(true);
        petrolChart.setScaleEnabled(true);

        // Values for x axis
        ArrayList<String> xVals = new ArrayList<>();
        //Values for y axis
        ArrayList<Entry> yVals = new ArrayList<>();

        // Get InputStream for String data
        InputStream sInStream = getResources().openRawResource(R.raw.petrol_prices);
        // Create Buffered Reader for String data stream
        BufferedReader sReader = new BufferedReader(
                new InputStreamReader(sInStream, StandardCharsets.UTF_8));
        String row;
        String[] rowSplit;
        int i = 0;
        // Read each row and check if it is the same as the user's query
        // Each row consist of Month,Price
        try {
            // Skip over the column headers
            sReader.readLine();
            // While there is a row to read
            while((row = sReader.readLine()) != null)
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

        LineDataSet ySet = new LineDataSet(yVals, "Y Data Set");
        ySet.setFillAlpha(100);
        ySet.setColor(Color.BLACK);
        ySet.setLineWidth(1f);
        ySet.setValueTextSize(10f);
        ySet.setValueTextColor(Color.RED);
        ySet.setDrawValues(false);
//        ySet.setCircleRadius(0f);
        ySet.setDrawCircles(false);
        ySet.setCircleColor(Color.GREEN);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(ySet);

        LineData data = new LineData(dataSets);

        petrolChart.setData(data);
        petrolChart.setBackgroundColor(Color.WHITE);
        petrolChart.getAxisRight().setEnabled(false);
        petrolChart.setDrawGridBackground(false);

        XAxis xAxis = petrolChart.getXAxis();
        YAxis yAxis = petrolChart.getAxisLeft();
        yAxis.setDrawLabels(true);
        yAxis.setCenterAxisLabels(true);
        String[] xValues = new String[xVals.size()];
        xValues = xVals.toArray(xValues);
        xAxis.setValueFormatter(new XAxisStringFormater(xValues));
//        xAxis.setGranularity(2f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        petrolChart.getDescription().setEnabled(false);
        petrolChart.getLegend().setEnabled(false);
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
}

class XAxisStringFormater implements IAxisValueFormatter {
    private String[] values;

    public XAxisStringFormater(String[] vals) {
        this.values = vals;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return values[(int)value];
    }
}
