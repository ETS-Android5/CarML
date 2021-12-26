package ie.tudublin.carml;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class PricePredictionActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton back_arrow;
    ImageButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.price_prediction_activity);

        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(this);
        submit = findViewById(R.id.submit_button);
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

    }

    public void readColumn() {

    }
}
