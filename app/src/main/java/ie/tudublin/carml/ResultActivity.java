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

import java.io.IOException;
import java.util.Enumeration;

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
        price.setText(String.valueOf(prediction));

    }

    public double predict(String query) {
        String[] query_split = query.split(",");
        int numAttributes = 4;
        int numInstances = 1;
        Attribute manufacturer = new Attribute("Make");
        Attribute model = new Attribute("Model");
        Attribute year = new Attribute("Year");
        Attribute price = new Attribute("MSRP");

        FastVector fvAttributes = new FastVector(numAttributes);
        fvAttributes.add(manufacturer);
        fvAttributes.add(model);
        fvAttributes.add(year);
        fvAttributes.add(price);

        Instances instances = new Instances("Rel", fvAttributes, numInstances);
        instances.setClassIndex(numAttributes - 1);

//        Instance user_query = new DenseInstance(numAttributes);
//        user_query.setValue(manufacturer, Float.parseFloat(query_split[0]));
//        user_query.setValue(model, Float.parseFloat(query_split[1]));
//        user_query.setValue(year, Float.parseFloat(query_split[2]));
        Instance user_query = new DenseInstance(numAttributes);
        user_query.setValue(manufacturer, 0);
        user_query.setValue(model, 0);
        user_query.setValue(year, 2001);

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
}
