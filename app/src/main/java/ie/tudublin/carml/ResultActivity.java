package ie.tudublin.carml;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case(R.id.back_arrow): {
                finish();
                break;
            }
            default: {
                break;
            }
        }
    }
}
