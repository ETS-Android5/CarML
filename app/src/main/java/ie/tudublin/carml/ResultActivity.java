package ie.tudublin.carml;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton back_arrow;
    ImageButton done;
    TextView manufacturer;
    TextView model;
    TextView year;
    TextView fuel_type;
    TextView horsepower;
    TextView transmission;
    TextView drivetrain;
    TextView num_doors;
    TextView body_type;
    TextView price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(this);
        done = findViewById(R.id.done_button);
        done.setOnClickListener(this);
        manufacturer = findViewById(R.id.result_manufacturer);
        model = findViewById(R.id.result_model);
        year = findViewById(R.id.result_year);
        fuel_type = findViewById(R.id.result_fuel_type);
        horsepower = findViewById(R.id.result_horsepower);
        transmission = findViewById(R.id.result_transmission);
        drivetrain = findViewById(R.id.result_drivetrain);
        num_doors =  findViewById(R.id.result_num_doors);
        body_type = findViewById(R.id.result_body_type);
        price = findViewById(R.id.result_price);
        displayResult();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case(R.id.back_arrow): {
                finish();
                break;
            }
            case(R.id.done_button): {
                finish();
            }
            default: {
                break;
            }
        }
    }

    public void displayResult() {
        Intent result = getIntent();
        String user_car = result.getStringExtra("user_car");
        String[] user_car_split = user_car.split(",");
        manufacturer.setText(user_car_split[0]);
        model.setText(user_car_split[1]);
        year.setText(user_car_split[2]);
        double prediction = predict(user_car);
        Locale currentLocale = getResources().getConfiguration().getLocales().get(0);
        price.setText(String.format(currentLocale,"%.0f", prediction));

    }

    public double predict(String query) {
        // query is manufacturer,model,year
        String[] query_split = query.split(",");
        int numAttributes = 4;
        int numInstances = 1;
//        Attribute manufacturer = new Attribute("Make");
//        Attribute model = new Attribute("Model");
//        Attribute year = new Attribute("Year");
//        Attribute price = new Attribute("MSRP");
        ArrayList<String> test = new ArrayList<>();
        test.add(query_split[0]);
        test.add(query_split[1]);
        test.add(query_split[2]);
        Attribute manufacturer = new Attribute("Make", test);
        Attribute model = new Attribute("Model", test);
        Attribute year = new Attribute("Year", test);
        Attribute price = new Attribute("MSRP");
//        ArrayList<String> attribute = new ArrayList<>();
//        attribute.add(query_split[0]);
//        attribute.add(query_split[1]);
//        attribute.add(query_split[2]);

//        Attribute all = new Attribute("All", attribute);

        ArrayList<Attribute> attributes= new ArrayList<>();
        attributes.add(manufacturer);
        attributes.add(model);
        attributes.add(year);
        attributes.add(price);
//        attributes.add(all);

        Instances instances = new Instances("Rel", attributes, numInstances);
        instances.setClassIndex(numAttributes - 1);
//        instances.setClassIndex(0);

        double[] encodedVals = getEncodedVals(query);

        Instance user_query = new DenseInstance(numAttributes);
//        user_query.setValue(manufacturer, encodedVals[0]);
//        user_query.setValue(model, encodedVals[1]);
//        user_query.setValue(year, Double.parseDouble(query_split[2]));
        user_query.setValue(manufacturer, String.valueOf(query_split[0]));
        user_query.setValue(model, String.valueOf(query_split[1]));
        user_query.setValue(year, String.valueOf(query_split[2]));
//        user_query.setValue(all, "");

        instances.add(user_query);

        try {
            Classifier rf = (Classifier)
                    weka.core.SerializationHelper.read(getAssets().open("carmlcat.model"));
            return rf.classifyInstance(instances.instance(0));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Get the encoded values so they can be passed to the model
    public double[] getEncodedVals(String query) {
        // query is manufacturer,model,year
        double[] result = {0,0};
        String[] query_split = query.split(",");
        String man = query_split[0];
        String mod = query_split[1];

        // Get InputStream for String data
        InputStream sInStream = getResources().openRawResource(R.raw.descriptive_data);
        // Create Buffered Reader for String data stream
        BufferedReader sReader = new BufferedReader(
                new InputStreamReader(sInStream, StandardCharsets.UTF_8));

        // Get InputStream for encoded data
        InputStream eInStream = getResources().openRawResource(R.raw.model_data);
        // Create Buffered Reader for encoded data stream
        BufferedReader eReader = new BufferedReader(
                new InputStreamReader(eInStream, StandardCharsets.UTF_8));

        String row;
        // Read each row and check if it is the same as the user's query
        try {
            // Skip over the column headers
            sReader.readLine();
            eReader.readLine();
            // While there is a row to read
            while((row = sReader.readLine()) != null)
            {
                // Split each row on the commas
                String[] splits = row.split(",");
                // If the manufacturer and model match the query
                if(splits[0].equals(man) && splits[1].equals(mod)) {
                    String eRow = eReader.readLine();
                    String[] eSplits = eRow.split(",");
                    result[0] = Double.parseDouble(eSplits[0]);
                    result[1] = Double.parseDouble(eSplits[1]);
                    String[] car_details = Arrays.copyOfRange(splits, 3, splits.length);
                    displayCarDetails(car_details);
                    break;
                }
                else {
                    eReader.readLine();
                }
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return result;
    }

    // Display the details of the car to the correct views
    public void displayCarDetails(String[] details) {
        // details is Engine Fuel Type,Engine HP,Transmission Type,
        // Driven_Wheels,Number of Doors,Vehicle Style,MSRP
        fuel_type.setText(formatString(details[0]));
        horsepower.setText(formatString(details[1]));
        transmission.setText(formatString(details[2]));
        drivetrain.setText(formatString(details[3]));
        num_doors.setText(formatString(details[4]));
        body_type.setText(formatString(details[5]));
    }

    // Take a string and format is so only the first character is uppercase
    // and the rest are lower case
    public String formatString(String s) {
        s = s.toLowerCase();
        return (s.substring(0,1).toUpperCase() + s.substring(1));
    }
}
