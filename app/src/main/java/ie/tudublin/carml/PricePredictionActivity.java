/* In this activity the user enters detail for a car they wish to see a predicted price for.
 * Author: Sean Coll
 * Date Created: 23/12/21
 * Last Modified: 14/02/22
 */
package ie.tudublin.carml;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PricePredictionActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ImageButton back_arrow;
    ImageButton submit;
    Spinner manufacturerDD;
    Spinner modelDD;
    Spinner yearDD;
    TextView modelLabel;
    TextView yearLabel;
    RelativeLayout modelSpinner;
    RelativeLayout yearSpinner;
    // Create ArrayLists that hold Manufacturer, Model and Year
    private final List<String> manufacturers = new ArrayList<>();
    private final List<String> models = new ArrayList<>();
    private final List<String> years = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.price_prediction_activity);

        // Set up the views
        setUpViews();
        // Load the manufacturers to start
        loadManufacturers();
    }

    // Sets up the views and makes the relevant ones hidden to start
    public void setUpViews() {
        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(this);
        submit = findViewById(R.id.submit_button);
        submit.setOnClickListener(this);
        manufacturerDD = findViewById(R.id.manufacturer);
        modelDD = findViewById(R.id.model);
        modelLabel = findViewById(R.id.model_label);
        modelLabel.setVisibility(View.GONE);
        modelSpinner = findViewById(R.id.model_layout);
        modelSpinner.setVisibility(View.GONE);
        yearDD = findViewById(R.id.year);
        yearLabel = findViewById(R.id.year_label);
        yearLabel.setVisibility(View.GONE);
        yearSpinner = findViewById(R.id.year_layout);
        yearSpinner.setVisibility(View.GONE);
        submit.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        submit.setAlpha(1F);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case(R.id.back_arrow): {
                finish();
                break;
            }
            case(R.id.submit_button): {
                submit.setAlpha(0.5F);
                submit.setOnClickListener(null);
                String user_car =   manufacturerDD.getSelectedItem() + "," +
                                    modelDD.getSelectedItem() + "," +
                                    yearDD.getSelectedItem();
                Intent result = new Intent(PricePredictionActivity.this, ResultActivity.class);
                result.putExtra("user_car", user_car);
                startActivity(result);
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()) {
            case(R.id.manufacturer): {
                if(!adapterView.getSelectedItem().toString().equals("Select One"))
                {
                    Log.i("CarML Spinners", "Selected: " + adapterView.getSelectedItem().toString());
                    loadModels(adapterView.getSelectedItem().toString());
                    modelLabel.setVisibility(View.VISIBLE);
                    modelSpinner.setVisibility(View.VISIBLE);
                    yearLabel.setVisibility(View.GONE);
                    yearSpinner.setVisibility(View.GONE);
                }
                break;
            }
            case(R.id.model): {
                if(!adapterView.getSelectedItem().toString().equals("Select One"))
                {
                    Log.i("CarML Spinners", "Selected: " + adapterView.getSelectedItem().toString());
                    loadYears(manufacturerDD.getSelectedItem() + "," + adapterView.getSelectedItem().toString());
                    yearLabel.setVisibility(View.VISIBLE);
                    yearSpinner.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                }
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void loadManufacturers() {
        manufacturers.add("Select One");
        Log.i("CarML Spinners", "Loading manufacturers");
        DatabaseAccess DBA = new DatabaseAccess();
        String manufacturersData = DBA.runThread("manufacturers", "");
        Log.i("CarML Spinners", "Received from thread: " + manufacturersData);
        if(!manufacturersData.substring(0,1).equals("S")) {
            String[] parsedManufacturers = parseData(manufacturersData,"Make");
            manufacturers.addAll(Arrays.asList(parsedManufacturers));
            Log.i("CarML Spinners", "Finished manufacturers");
        }
        else {
            Toast.makeText(this, "Server Unavailable",Toast.LENGTH_LONG);
        }


        ArrayAdapter<String> manAdapter =
                new ArrayAdapter<>(this, R.layout.spinner_item, manufacturers);
        manAdapter.setDropDownViewResource(R.layout.spinner_item);
        manufacturerDD.setAdapter(manAdapter);
        manufacturerDD.setOnItemSelectedListener(this);
        manAdapter.notifyDataSetChanged();
    }

    public void loadModels(String man) {
        // Empty the list
        models.clear();
        models.add("Select One");
        Log.i("CarML Spinners", "Loading models");
        DatabaseAccess DBA = new DatabaseAccess();
        String modelsData = DBA.runThread("models", man +", , ");
        String[] parsedModels = parseData(modelsData,"Model");
        models.addAll(Arrays.asList(parsedModels));
        Log.i("CarML Spinners", "Finished models");

        ArrayAdapter<String> modAdapter =
                new ArrayAdapter<>(this, R.layout.spinner_item, models);
        modAdapter.setDropDownViewResource(R.layout.spinner_item);
        modelDD.setAdapter(modAdapter);
        modelDD.setOnItemSelectedListener(this);
        modAdapter.notifyDataSetChanged();
    }

    public void loadYears(String car) {
        // Empty the list
        years.clear();
        Log.i("CarML Spinners", "Loading years");
        DatabaseAccess DBA = new DatabaseAccess();
        String yearsData = DBA.runThread("years", car +", ");
        String[] parsedYears = parseData(yearsData,"Year");
        years.addAll(Arrays.asList(parsedYears));
        Log.i("CarML Spinners", "Finished years");

        ArrayAdapter<String> yearAdapter =
                new ArrayAdapter<>(this, R.layout.spinner_item, years);
        yearAdapter.setDropDownViewResource(R.layout.spinner_item);
        yearDD.setAdapter(yearAdapter);
        yearDD.setOnItemSelectedListener(this);
        yearAdapter.notifyDataSetChanged();
    }

    public String[] parseData (String raw, String name) {
        try {
            JSONArray jsonArray = new JSONArray(raw);
            String[] data = new String[jsonArray.length()];
            for(int i = 0; i < jsonArray.length(); i++) {
                // Create a new StringBuilder each time so it is clear
                JSONObject obj = jsonArray.getJSONObject(i);
                data[i] = obj.getString(name);
            }
            return data;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
