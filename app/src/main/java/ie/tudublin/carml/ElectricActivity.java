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
        }
    }

    public void setUpViews() {
        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(this);
        easyGo = findViewById(R.id.easygo_layout);
        easyGo.setOnClickListener(this);
    }
}
