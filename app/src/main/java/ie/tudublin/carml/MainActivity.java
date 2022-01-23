/* This is the Main Activity. It starts on the home screen and displays options to the user
* Author: Sean Coll
* Date Created: 12/12/21
* Last Modified: 21/1/22
*/

package ie.tudublin.carml;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton price_prediction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        price_prediction = findViewById(R.id.price_prediction_button);
        price_prediction.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case(R.id.price_prediction_button): {
                Intent price_prediction = new Intent(MainActivity.this, PricePredictionActivity.class);
                startActivity(price_prediction);
                break;
            }
            default: {
                break;
            }
        }
    }
}