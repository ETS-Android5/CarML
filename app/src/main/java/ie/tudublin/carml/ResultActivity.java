package ie.tudublin.carml;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import weka.classifiers.trees.RandomForest;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton back_arrow;
    ImageButton done;
    TextView manufacturer;
    TextView model;
    TextView year;

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
        RandomForest rf = new RandomForest();
        rf.setNumIterations(100);
        rf.setMaxDepth(10);
        rf.setSeed(0);
    }
}
