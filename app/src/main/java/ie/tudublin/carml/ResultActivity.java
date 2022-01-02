package ie.tudublin.carml;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton back_arrow;
    ImageButton done;
    TextView manufacturer;
    TextView model;
    TextView year;
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
        Attribute manufacturer = new Attribute("Make");
        Attribute model = new Attribute("Model");
        Attribute year = new Attribute("Year");
        Attribute price = new Attribute("MSRP");

        ArrayList<Attribute> attributes= new ArrayList<>();
        attributes.add(manufacturer);
        attributes.add(model);
        attributes.add(year);
        attributes.add(price);

        Instances instances = new Instances("Rel", attributes, numInstances);
        instances.setClassIndex(numAttributes - 1);

        double[] encodedVals = getEncodedVals(query_split[0], query_split[1]);

        Instance user_query = new DenseInstance(numAttributes);
        user_query.setValue(manufacturer, encodedVals[0]);
        user_query.setValue(model, encodedVals[1]);
        user_query.setValue(year, Double.parseDouble(query_split[2]));

        instances.add(user_query);

        try {
            Classifier rf = (Classifier)
                    weka.core.SerializationHelper.read(getAssets().open("carml.model"));
            return rf.classifyInstance(instances.instance(0));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public double[] getEncodedVals(String man, String mod) {
        double[] result = {0,0};

        // Get InputStream for String data
        InputStream sInStream = getResources().openRawResource(R.raw.sorted_data);
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
            while((row = sReader.readLine()) != null)
            {
                // Split each row on the commas
                String[] splits = row.split(",");
                // If the manufacturer and model are the same
                if(splits[0].equals(man) && splits[1].equals(mod)) {
                    String eRow = eReader.readLine();
                    String[] eSplits = eRow.split(",");
                    result[0] = Double.parseDouble(eSplits[0]);
                    result[1] = Double.parseDouble(eSplits[1]);
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
}
