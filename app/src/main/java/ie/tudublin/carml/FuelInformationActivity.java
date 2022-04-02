/* In this Activity, the user must choose between Petrol, Diesel and Electric fuel types to see
 * information about.
 * Author: Sean Coll
 * Date Created: 26/02/22
 * Last Modified: 08/03/22
 */

package ie.tudublin.carml;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

public class FuelInformationActivity extends Activity implements View.OnClickListener {

    RelativeLayout petrol;
    RelativeLayout diesel;
    RelativeLayout electric;
    ImageButton back_arrow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fuel_information);
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
            case(R.id.petrol_layout): {
                Intent petrol = new Intent(FuelInformationActivity.this, PetrolActivity.class);
                startActivity(petrol);
                break;
            }
            case(R.id.diesel_layout): {
                Intent diesel = new Intent(FuelInformationActivity.this, DieselActivity.class);
                startActivity(diesel);
                break;
            }
            case(R.id.electric_layout): {
                Intent electric = new Intent(FuelInformationActivity.this, ElectricActivity.class);
                startActivity(electric);
                break;
            }
        }
    }

    public void setUpViews() {
        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(this);
        petrol = findViewById(R.id.petrol_layout);
        petrol.setOnClickListener(this);
        diesel = findViewById(R.id.diesel_layout);
        diesel.setOnClickListener(this);
        electric = findViewById(R.id.electric_layout);
        electric.setOnClickListener(this);
    }
}
