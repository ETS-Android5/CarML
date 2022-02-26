package ie.tudublin.carml;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class FuelInformationActivity extends Activity implements View.OnClickListener {

    TextView petrol;
    TextView diesel;
    TextView electric;
    ImageButton back_arrow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fuel_information);
        // Set up views
        setUpViews();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case(R.id.back_arrow): {
                finish();
                break;
            }
            case(R.id.petrol_button): {
                Intent petrol = new Intent(FuelInformationActivity.this, PetrolActivity.class);
                startActivity(petrol);
                break;
            }
            case(R.id.diesel_button): {
                Intent diesel = new Intent(FuelInformationActivity.this, DieselActivity.class);
                startActivity(diesel);
                break;
            }
            case(R.id.electric_button): {
                Intent electric = new Intent(FuelInformationActivity.this, ElectricActivity.class);
                startActivity(electric);
                break;
            }
        }
    }

    public void setUpViews() {
        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(this);
        petrol = findViewById(R.id.petrol_button);
        petrol.setOnClickListener(this);
        diesel = findViewById(R.id.diesel_button);
        diesel.setOnClickListener(this);
        electric = findViewById(R.id.electric_button);
        electric.setOnClickListener(this);
    }
}
