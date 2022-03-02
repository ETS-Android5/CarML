/* In this activity the user enters detail for a car they wish to see a predicted price for.
 * Author: Sean Coll
 * Date Created: 23/12/21
 * Last Modified: 20/02/22
 */
package ie.tudublin.carml;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PricePredictionActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ImageButton back_arrow;
    TextView submit;
    Spinner manufacturerDD;
    Spinner modelDD;
    Spinner yearDD;
    TextView infoButton;
    TextView modelLabel;
    TextView yearLabel;
    RelativeLayout modelSpinner;
    RelativeLayout yearSpinner;
    PopupWindow pleaseWaitWindow = null;
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

    @Override
    protected void onResume() {
        super.onResume();
        submit.setAlpha(1F);
        submit.setOnClickListener(this);
        if(pleaseWaitWindow != null && pleaseWaitWindow.isShowing())
            pleaseWaitWindow.dismiss();
    }

    // Sets up the views and makes the relevant ones hidden to start
    public void setUpViews() {
        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(this);
        submit = findViewById(R.id.submit_button);
        submit.setOnClickListener(this);
        manufacturerDD = findViewById(R.id.manufacturer);
        modelDD = findViewById(R.id.model);
        infoButton = findViewById(R.id.information_button);
        infoButton.setOnClickListener(this);
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
    public void onClick(View view) {
        switch(view.getId()) {
            case(R.id.back_arrow): {
                finish();
                break;
            }
            case(R.id.submit_button): {
                RelativeLayout parent = findViewById(R.id.main);
                // Inflate the layout
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View pleaseWait = inflater.inflate(R.layout.please_wait_popup, parent, false);

                // Create the popup
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                pleaseWaitWindow = new PopupWindow(pleaseWait, width, height, false);
                // Display the popup
                pleaseWaitWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

                submit.setAlpha(0.5F);
                submit.setOnClickListener(null);
                String user_car =   manufacturerDD.getSelectedItem() + "," +
                                    modelDD.getSelectedItem() + "," +
                                    yearDD.getSelectedItem();
                Intent result = new Intent(PricePredictionActivity.this, ResultActivity.class);
                // user_car is manufacturer,model,year
                result.putExtra("user_car", user_car);
                startActivity(result);
                break;
            }
            case(R.id.information_button): {
                RelativeLayout parent = findViewById(R.id.main);
                // Inflate the layout
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View info = inflater.inflate(R.layout.information_button_popup, parent, false);

                // Create the popup
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                PopupWindow infoWindow = new PopupWindow(info, width, height, true);

                // Display the popup
                infoWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

                // Make the popup disappear when tapped
                info.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        infoWindow.dismiss();
                        return true;
                    }
                });
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
        JSONParser parser = new JSONParser();
        manufacturers.add("Select One");
        Log.i("CarML Spinners", "Loading manufacturers");
        DatabaseAccess DBA = new DatabaseAccess();
        String manufacturersData = DBA.runThread("manufacturers", "");
        Log.i("CarML Spinners", "Received from thread: " + manufacturersData);
        if(!manufacturersData.startsWith("ERROR")) {
            String[] parsedManufacturers = parser.parseData(manufacturersData,"Make");
            manufacturers.addAll(Arrays.asList(parsedManufacturers));
            Log.i("CarML Spinners", "Finished manufacturers");
        }

        ArrayAdapter<String> manAdapter =
                new ArrayAdapter<>(this, R.layout.spinner_item, manufacturers);
        manAdapter.setDropDownViewResource(R.layout.spinner_item);
        manufacturerDD.setAdapter(manAdapter);
        manufacturerDD.setOnItemSelectedListener(this);
        manAdapter.notifyDataSetChanged();
    }

    public void loadModels(String man) {
        submit.setVisibility(View.INVISIBLE);
        JSONParser parser = new JSONParser();
        // Empty the list
        models.clear();
        models.add("Select One");
        Log.i("CarML Spinners", "Loading models");
        DatabaseAccess DBA = new DatabaseAccess();
        String modelsData = DBA.runThread("models", man +", , ");
        String[] parsedModels = parser.parseData(modelsData,"Model");
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
        JSONParser parser = new JSONParser();
        // Empty the list
        years.clear();
        Log.i("CarML Spinners", "Loading years");
        DatabaseAccess DBA = new DatabaseAccess();
        String yearsData = DBA.runThread("years", car +", ");
        String[] parsedYears = parser.parseData(yearsData,"Year");
        years.addAll(Arrays.asList(parsedYears));
        Log.i("CarML Spinners", "Finished years");

        ArrayAdapter<String> yearAdapter =
                new ArrayAdapter<>(this, R.layout.spinner_item, years);
        yearAdapter.setDropDownViewResource(R.layout.spinner_item);
        yearDD.setAdapter(yearAdapter);
        yearDD.setOnItemSelectedListener(this);
        yearAdapter.notifyDataSetChanged();
    }
}
