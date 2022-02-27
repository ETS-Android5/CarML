package ie.tudublin.carml;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

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

        //Values for y axis
        ArrayList<Entry> yVals = new ArrayList<>();

        yVals.add(new Entry(0, 5f));
        yVals.add(new Entry(1, 9f));
        yVals.add(new Entry(2, 7f));
        yVals.add(new Entry(3, 9f));
        yVals.add(new Entry(4, 5f));

        LineDataSet ySet = new LineDataSet(yVals, "Y Data Set");
        ySet.setFillAlpha(100);
        ySet.setColor(Color.BLACK);
        ySet.setLineWidth(3f);
        ySet.setValueTextSize(10f);
        ySet.setValueTextColor(Color.RED);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(ySet);

        LineData data = new LineData(dataSets);

        petrolChart.setData(data);
        petrolChart.setBackgroundColor(Color.WHITE);
        petrolChart.getAxisRight().setEnabled(false);

        XAxis xAxis = petrolChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

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
