/* This Activity presents links to different websites containing information about electric vehicles
 * and electric vehicle charger suppliers.
 * Author: Sean Coll
 * Date Created: 26/02/22
 * Last Modified:02/03/22
 */

package ie.tudublin.carml;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

public class ElectricActivity extends Activity implements View.OnClickListener {

    ImageButton back_arrow;
    RelativeLayout easyGo;
    RelativeLayout ecarInfra;
    RelativeLayout evChargingIreland;
    RelativeLayout wallbox;
    RelativeLayout seai;
    RelativeLayout bordGaisEnergy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.electric);
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
            case(R.id.easygo_layout): {
                Intent easyGoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://easygo.ie/charging-at-home/"));
                startActivity(easyGoIntent);
                break;
            }
            case(R.id.ecar_infra_layout): {
                Intent ecarInfraIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ecarinfra.ie/"));
                startActivity(ecarInfraIntent);
                break;
            }
            case(R.id.ev_charging_ireland_layout): {
                Intent evChargingIrelandIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://evchargingireland.ie/"));
                startActivity(evChargingIrelandIntent);
                break;
            }
            case(R.id.wallbox_layout): {
                Intent wallboxIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wallbox.com/en_ie"));
                startActivity(wallboxIntent);
                break;
            }
            case(R.id.seai_layout): {
                Intent seaiIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.seai.ie/grants/electric-vehicle-grants/electric-vehicle-home-charger-grant/"));
                startActivity(seaiIntent);
                break;
            }
            case(R.id.bord_gais_energy_layout): {
                Intent bordGaisEnergyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.bordgaisenergy.ie/home/ev-charger"));
                startActivity(bordGaisEnergyIntent);
                break;
            }
        }
    }

    public void setUpViews() {
        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(this);
        easyGo = findViewById(R.id.easygo_layout);
        easyGo.setOnClickListener(this);
        ecarInfra = findViewById(R.id.ecar_infra_layout);
        ecarInfra.setOnClickListener(this);
        evChargingIreland = findViewById(R.id.ev_charging_ireland_layout);
        evChargingIreland.setOnClickListener(this);
        wallbox = findViewById(R.id.wallbox_layout);
        wallbox.setOnClickListener(this);
        seai = findViewById(R.id.seai_layout);
        seai.setOnClickListener(this);
        bordGaisEnergy = findViewById(R.id.bord_gais_energy_layout);
        bordGaisEnergy.setOnClickListener(this);
    }
}
