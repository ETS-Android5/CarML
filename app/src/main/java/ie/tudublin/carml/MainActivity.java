/* This is the Main Activity. It starts on the home screen and displays options to the user
* Author: Sean Coll
* Date Created: 12/12/21
* Last Modified: 21/1/22
*/

package ie.tudublin.carml;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView price_prediction;
    PopupWindow pleaseWaitWindow = null;

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
                price_prediction.setAlpha(0.5f);
                testServerConnection();
                price_prediction.setAlpha(1f);
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        price_prediction.setAlpha(1f);
        if(pleaseWaitWindow != null && pleaseWaitWindow.isShowing())
            pleaseWaitWindow.dismiss();
    }

    public void displayPopup() {
        // Inflate the layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.server_unavailable_popup, null);

        // Create the popup
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        PopupWindow popupWindow = new PopupWindow(popup, width, height, true);

        // Display the popup
        RelativeLayout parent = findViewById(R.id.main);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

        // Make the popup disappear when tapped
        popup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void testServerConnection() {
        // Inflate the layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View pleaseWait = inflater.inflate(R.layout.please_wait_popup, null);

        // Create the popup
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        pleaseWaitWindow = new PopupWindow(pleaseWait, width, height, false);
        // Display the popup
        RelativeLayout parent = findViewById(R.id.main);
        pleaseWaitWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

        DatabaseAccess DBA = new DatabaseAccess();
        String response = DBA.runThread("ping","");
        if(response.contains("ERROR")) {
            // The server is unavailable
            displayPopup();
            pleaseWaitWindow.dismiss();
        }
        else {
            Intent price_prediction = new Intent(MainActivity.this, PricePredictionActivity.class);
            startActivity(price_prediction);
        }
    }
}