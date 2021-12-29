package ie.tudublin.carml;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PricePredictionActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton back_arrow;
    ImageButton submit;
    Spinner manufacturerDD;
    Spinner modelDD;
    NumberPicker year;
    // Create ArrayLists that hold Manufacturer and Model
    private List<String> manufacturers = new ArrayList<>();
    private List<String> models = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.price_prediction_activity);

        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(this);
        submit = findViewById(R.id.submit_button);
        submit.setOnClickListener(this);
        year = findViewById(R.id.year);
        year.setMinValue(1990);
        year.setMaxValue(2017);
        prepSpinners();
        manufacturerDD = findViewById(R.id.manufacturer);
        modelDD = findViewById(R.id.model);
        ArrayAdapter<String> manAdapter =
                new ArrayAdapter<>(this, R.layout.spinner_item, manufacturers);
        manAdapter.setDropDownViewResource(R.layout.spinner_item);
        manufacturerDD.setAdapter(manAdapter);
        ArrayAdapter<String> modAdapter =
                new ArrayAdapter<>(this, R.layout.spinner_item, models);
        modAdapter.setDropDownViewResource(R.layout.spinner_item);
        modelDD.setAdapter(modAdapter);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case(R.id.back_arrow): {
                finish();
                break;
            }
            case(R.id.submit_button): {
                String man = manufacturerDD.getSelectedItem().toString();
                String mod = modelDD.getSelectedItem().toString();
                String year_val = String.valueOf(year.getValue());
                Log.d("Spinner", man + "," + mod + "," + year_val);
                Intent result = new Intent(PricePredictionActivity.this, ResultActivity.class);
                startActivity(result);
                break;
            }
            default: {
                break;
            }
        }
    }

    public void prepSpinners() {
        // Add in the first value for the dropdown menus
        manufacturers.add("Select One");
        models.add("Select One");

        // Get InputStream for data
        InputStream inStream = getResources().openRawResource(R.raw.kaggle_data_clean);
        // Create Buffered Reader for stream
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inStream, StandardCharsets.UTF_8));

        // i is used to check if the previous item in the list is the same as the current
        int ma = 1;
        int mo = 1;
        String row = "f";
        // Read each row and parse the correct parts into the correct lists
        try {
            // Skip over the column headers
            reader.readLine();
            while((row = reader.readLine()) != null)
            {
                // Split each row on the commas
                String[] splits = row.split(",");

                if (ma > 0) {
                    if (!manufacturers.get(ma-1).equals(splits[0])) {
                        manufacturers.add(splits[0]);
                        ma++;
                    }
                }

                if (mo > 0) {
                    if (!models.get(mo-1).equals(splits[1])) {
                        models.add(splits[1]);
                        mo++;
                    }
                }
            }

        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
